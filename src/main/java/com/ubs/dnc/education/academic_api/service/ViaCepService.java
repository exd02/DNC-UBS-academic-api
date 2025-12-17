package com.ubs.dnc.education.academic_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubs.dnc.education.academic_api.dto.response.ViaCepResponse;
import com.ubs.dnc.education.academic_api.exception.CepInvalidoException;
import com.ubs.dnc.education.academic_api.exception.CepNaoEncontradoException;
import com.ubs.dnc.education.academic_api.exception.CepErroConsultaException;
import com.ubs.dnc.education.academic_api.model.Endereco;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ViaCepService {
    private static final String VIACEP_URL = "https://viacep.com.br/ws/%s/json/";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ViaCepService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public Endereco buscarEndereco(String cep) {
        try {
            String url = String.format(VIACEP_URL, cep);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() == 400) {
                throw new CepInvalidoException("CEP com formato invalido");
            }

            if (response.statusCode() != 200) {
                throw new CepErroConsultaException("Erro ao consultar ViaCEP: HTPP " + response.statusCode());
            }

            // requisição valida mas o cep nao existe
            String responseBody = response.body();
            if (responseBody.contains("\"erro\": \"true\"")) {
                throw new CepNaoEncontradoException("CEP " + cep + " não encontrado");
            }

            ViaCepResponse viaCepResponse = objectMapper.readValue(responseBody, ViaCepResponse.class);

            // valida se os campos essenciais vieram preenchidos
            if (viaCepResponse.getCidade() == null || viaCepResponse.getEstado() == null) {
                throw new CepNaoEncontradoException("CEP " + cep + " retornou dados incompletos");
            }

            return converterParaEndereco(cep, viaCepResponse);

        } catch (Exception e) {
            throw new CepErroConsultaException("Erro ao consultar CEP: " + e.getMessage());
        }
    }

    private Endereco converterParaEndereco(String cep, ViaCepResponse response) {
        return Endereco.builder()
                .cep(cep)
                .bairro(response.getBairro())
                .estado(response.getEstado())
                .cidade(response.getCidade())
                .build();
    }
}


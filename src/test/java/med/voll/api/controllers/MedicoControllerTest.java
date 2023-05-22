package med.voll.api.controllers;

import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.medico.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
public class MedicoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosCadastroMedico> dadosCadastroMedicoJson;

    @Autowired
    private JacksonTester<DadosDetalhamentoMedico> dadosDetalhamentoMedicoJson;

    @MockBean
    private MedicoRepository repository;

    @Test
    @DisplayName("Deveria devolver o código HTTP 200 quando informações estão válidas")
    @WithMockUser
    void cadastrarCenario1() throws Exception {
        // Recebe um dadosCadastroMedico e uma UriComponentsBuilder
        var especialidade = Especialidade.CARDIOLOGIA;
        var endereco = new DadosEndereco("Rua AAA", "Bairro Novo", "85274162", "Recife", "PE",
                null, null);
        var dadosCadastroMedico = new DadosCadastroMedico("Medico1", "medico@vollmed.com", "33414247",
                "123456", especialidade, endereco);

        var medico = new Medico(dadosCadastroMedico);

        var dadosDetalhamentoMedico = new DadosDetalhamentoMedico(medico);

        when(repository.save(any())).thenReturn(medico);

        var response = mvc.perform(MockMvcRequestBuilders.post("/medicos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosCadastroMedicoJson.write(dadosCadastroMedico).getJson()))
                .andReturn().getResponse();

        var jsonEsperado = dadosDetalhamentoMedicoJson.write(dadosDetalhamentoMedico).getJson();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(jsonEsperado, response.getContentAsString());

    }
}

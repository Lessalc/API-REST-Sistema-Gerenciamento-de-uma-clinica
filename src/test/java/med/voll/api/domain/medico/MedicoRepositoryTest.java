package med.voll.api.domain.medico;

import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.paciente.DadosCadastroPacientes;
import med.voll.api.domain.paciente.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class MedicoRepositoryTest {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private TestEntityManager em;

    private DadosEndereco dadosEndereco(){
        return new DadosEndereco(
                "rua XPTO",
                "bairro",
                "41002000",
                "Brasilia",
                "DF",
                null,
                null
        );
    }

    private DadosCadastroPacientes dadosPacientes(String nome, String email, String cpf){
        return new DadosCadastroPacientes(
                nome,
                email,
                "33414247",
                cpf,
                dadosEndereco()
        );
    }

    private DadosCadastroMedico dadosMedico(String nome, String email, String crm,
                                            Especialidade especialidade){
        return new DadosCadastroMedico(
                nome,
                email,
                "33414247",
                crm,
                especialidade,
                dadosEndereco()
        );
    }

    private Paciente cadastrarPaciente(String nome, String email, String cpf){
        var paciente = new Paciente(dadosPacientes(nome, email, cpf));
        em.persist(paciente);
        return paciente;
    }

    private Medico cadastrarMedico(String nome, String email, String crm,
                                   Especialidade especialidade){
        var medico = new Medico(dadosMedico(nome, email, crm, especialidade));
        em.persist(medico);
        return medico;
    }

    private void cadastrarConsulta(Medico medico, Paciente paciente, LocalDateTime data){
        em.persist(new Consulta(null, medico, paciente, data));
    }

    @Test
    @DisplayName("Deveria devolver null quando unico medico cadastrado nao esta livre na data")
    void escolherMedicoAleatorioLivreNaDataCenario1() {
        var proximaSegundaAsDez = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10,0);
        var medico = cadastrarMedico("Medico", "medico@voll.med","123456", Especialidade.CARDIOLOGIA);
        var paciente = cadastrarPaciente("Paciente", "paciente@email.com", "123.432.918-98");
        cadastrarConsulta(medico, paciente, proximaSegundaAsDez);
        var medicoLivre = medicoRepository.escolherMedicoAleatorioLivreNaData(Especialidade.CARDIOLOGIA,
                proximaSegundaAsDez);
        assertNull(medicoLivre);
    }

    @Test
    @DisplayName("Deveria devolver medico quando ele estiver disponivel na data")
    void escolherMedicoAleatorioLivreNaDataCenario2() {
        var proximaSegundaAsDez = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10,0);
        var medico = cadastrarMedico("Medico", "medico@voll.med","123456", Especialidade.CARDIOLOGIA);

        var medicoLivre = medicoRepository.escolherMedicoAleatorioLivreNaData(Especialidade.CARDIOLOGIA,
                proximaSegundaAsDez);
        assertEquals(medico, medicoLivre);
    }
}

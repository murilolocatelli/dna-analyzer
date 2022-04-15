package br.com.mercadolivre.dnaanalyzer.integration.test;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.mercadolivre.dnaanalyzer.dto.DnaDto;
import br.com.mercadolivre.dnaanalyzer.enumeration.DnaType;
import br.com.mercadolivre.dnaanalyzer.model.DnaHistory;
import br.com.mercadolivre.dnaanalyzer.repository.DnaHistoryRepository;
import br.com.mercadolivre.dnaanalyzer.service.JsonService;
import br.com.mercadolivre.dnaanalyzer.test.BaseIntegrationTest;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class DnaControllerTest extends BaseIntegrationTest {

    private static final String GET_STATUS = "/stats";
    private static final String POST_DNA = "/simian";

    @Autowired
    private DnaHistoryRepository dnaHistoryRepository;

    @Autowired
    private JsonService jsonService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void before() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void methodNotAllowed() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = post(GET_STATUS);

        this.mockMvc
            .perform(requestBuilder)
            .andExpect(status().isMethodNotAllowed())
            .andExpect(jsonPath("$.[0].message", is("Method not allowed")));
    }

    @Test
    public void postDnaMissingContent() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = post(POST_DNA)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        this.mockMvc
            .perform(requestBuilder)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.[0].message", is("Malformed request")));
    }

    @Test
    public void postDnaMissingDna() throws Exception {
        final DnaDto dnaDto = DnaDto.builder().dna(null).build();

        final MockHttpServletRequestBuilder requestBuilder = post(POST_DNA)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(this.jsonService.toJsonString(dnaDto));

        this.mockMvc
            .perform(requestBuilder)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.[0].message", is("Missing parameter dna")));
    }

    @Test
    public void postDnaTypeMismatch() throws Exception {
        final List<String> dna = Collections.emptyList();

        final DnaDto dnaDto = DnaDto.builder().dna(dna).build();

        final ObjectNode dnaDtoNode = this.jsonService.toObjectNode(dnaDto);
        dnaDtoNode.put("dna", "CGATC");

        final MockHttpServletRequestBuilder requestBuilder = post(POST_DNA)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(dnaDtoNode.toString());

        this.mockMvc
            .perform(requestBuilder)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath(
                "$.[0].message", is("Invalid parameter dna - it must be filled with a array")));
    }

    @Test
    public void postDnaEmptyDna() throws Exception {
        final List<String> dna = Collections.emptyList();

        final DnaDto dnaDto = DnaDto.builder().dna(dna).build();

        final MockHttpServletRequestBuilder requestBuilder = post(POST_DNA)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(this.jsonService.toJsonString(dnaDto));

        this.mockMvc
            .perform(requestBuilder)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.[0].message", is("Missing parameter dna")));
    }

    @Test
    public void postDnaInvalidNotSquareMatrix() throws Exception {
        final List<String> dna = List.of("AAAA", "CCCC");

        final DnaDto dnaDto = DnaDto.builder().dna(dna).build();

        final MockHttpServletRequestBuilder requestBuilder = post(POST_DNA)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(this.jsonService.toJsonString(dnaDto));

        this.mockMvc
            .perform(requestBuilder)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.[0].message", is("Invalid DNA: The DNA matrix is not square")));
    }

    @Test
    public void postDnaInvalidLetter() throws Exception {
        final List<String> dna = List.of("BBBB", "TGAC", "AAAA", "TGAC");

        final DnaDto dnaDto = DnaDto.builder().dna(dna).build();

        final MockHttpServletRequestBuilder requestBuilder = post(POST_DNA)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(this.jsonService.toJsonString(dnaDto));

        this.mockMvc
            .perform(requestBuilder)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.[0].message", is("Invalid DNA: Invalid letter(s)")));
    }

    @Test
    public void postDnaHumanConflict() throws Exception {
        this.dnaHistoryRepository.save(this.dnaHistoryHuman());

        final List<String> dna = this.convertStringToList(this.dnaHistoryHuman().getDna());

        final DnaDto dnaDto = DnaDto.builder().dna(dna).build();

        final MockHttpServletRequestBuilder requestBuilder = post(POST_DNA)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(this.jsonService.toJsonString(dnaDto));

        this.mockMvc
            .perform(requestBuilder)
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.[0].message", is("DNA already exists")));
    }

    @Test
    public void postDnaSimianConflict() throws Exception {
        this.dnaHistoryRepository.save(this.dnaHistorySimian());

        final List<String> dna = this.convertStringToList(this.dnaHistorySimian().getDna());

        final DnaDto dnaDto = DnaDto.builder().dna(dna).build();

        final MockHttpServletRequestBuilder requestBuilder = post(POST_DNA)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(this.jsonService.toJsonString(dnaDto));

        this.mockMvc
            .perform(requestBuilder)
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.[0].message", is("DNA already exists")));
    }

    @Test
    public void postDnaHumanForbidden() throws Exception {
        final List<String> dna = this.convertStringToList(this.dnaHistoryHuman().getDna());

        final DnaDto dnaDto = DnaDto.builder().dna(dna).build();

        final MockHttpServletRequestBuilder requestBuilder = post(POST_DNA)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(this.jsonService.toJsonString(dnaDto));

        this.mockMvc
            .perform(requestBuilder)
            .andExpect(status().isForbidden())
            .andExpect(jsonPath(
                "$.[0].message", is("Forbidden: You don't have permission to access on this server")));
    }

    @Test
    public void postDnaSimian() throws Exception {
        final List<String> dna = this.convertStringToList(this.dnaHistorySimian().getDna());

        final DnaDto dnaDto = DnaDto.builder().dna(dna).build();

        final MockHttpServletRequestBuilder requestBuilder = post(POST_DNA)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(this.jsonService.toJsonString(dnaDto));

        this.mockMvc
            .perform(requestBuilder)
            .andExpect(status().isOk());
    }

    @Test
    public void getStatsDnaHistoryNotFound() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = get(GET_STATUS);

        this.mockMvc
            .perform(requestBuilder)
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.[0].message", is("DNA History not found")));
    }

    @Test
    public void getStatsMutant() throws Exception {
        this.dnaHistoryRepository.save(this.dnaHistorySimian());

        final MockHttpServletRequestBuilder requestBuilder = get(GET_STATUS);

        this.mockMvc
            .perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count_mutant_dna", is(1)))
            .andExpect(jsonPath("$.count_human_dna", is(0)))
            .andExpect(jsonPath("$.ratio").doesNotExist());
    }

    @Test
    public void getStatsHuman() throws Exception {
        this.dnaHistoryRepository.save(this.dnaHistoryHuman());

        final MockHttpServletRequestBuilder requestBuilder = get(GET_STATUS);

        this.mockMvc
            .perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count_mutant_dna", is(0)))
            .andExpect(jsonPath("$.count_human_dna", is(1)))
            .andExpect(jsonPath("$.ratio").doesNotExist());
    }

    @Test
    public void getStatsMutantAndHuman() throws Exception {
        this.dnaHistoryRepository.save(this.dnaHistorySimian());
        this.dnaHistoryRepository.save(this.dnaHistoryHuman());

        final MockHttpServletRequestBuilder requestBuilder = get(GET_STATUS);

        this.mockMvc
            .perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count_mutant_dna", is(1)))
            .andExpect(jsonPath("$.count_human_dna", is(1)))
            .andExpect(jsonPath("$.ratio", is(1.0)));
    }

    private DnaHistory dnaHistoryHuman() {
        final String[] dna = {"ACTG", "TGAC", "ACTG", "TGAC"};
        return DnaHistory.builder()
            .dna(Arrays.toString(dna))
            .dnaType(DnaType.HUMAN)
            .build();
    }

    private DnaHistory dnaHistorySimian() {
        final String[] dna = {"AAAA", "TGAC", "AAAA", "TGAC"};
        return DnaHistory.builder()
            .dna(Arrays.toString(dna))
            .dnaType(DnaType.SIMIAN)
            .build();
    }

    private List<String> convertStringToList(final String value) {
        final String dna = value.replace("[", "").replace("]", "");
        return Arrays.asList(dna.split(", "));
    }

}

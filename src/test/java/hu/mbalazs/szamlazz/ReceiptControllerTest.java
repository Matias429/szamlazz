/*
package hu.mbalazs.szamlazz;

import hu.mbalazs.szamlazz.dtos.ReceiptDto;
import hu.mbalazs.szamlazz.services.ReceiptPersistenceService;
import hu.mbalazs.szamlazz.services.ReceiptWebClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest
class ReceiptControllerTest {

	ClassPathResource resource = new ClassPathResource("exampleresponse.xml");

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ReceiptWebClientService receiptWebClientService;

	@MockitoBean
	private ReceiptPersistenceService receiptPersistenceService;

	@Test
	void testPersistenceServiceSavesXml() {
        try {
            String xml = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
			ReceiptDto savedReceipt = receiptPersistenceService.saveReceiptFromXml(xml);
			assertEquals("CID123", savedReceipt.getAlap().getHivasAzonosito());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
*/

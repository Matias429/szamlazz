package hu.mbalazs.szamlazz.services;

import hu.mbalazs.szamlazz.dtos.ReceiptDto;
import hu.mbalazs.szamlazz.dtos.ResponseDto;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.stereotype.Service;

import java.io.StringReader;

@Service
public class XmlParserService {
    public ReceiptDto parse(String xml) {
        try {
            JAXBContext context = JAXBContext.newInstance(ResponseDto.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            ResponseDto response = (ResponseDto) unmarshaller.unmarshal(new StringReader(xml));

            if (Boolean.FALSE.equals(response.getSikeres())) {
                throw new IllegalStateException("Számlázz.hu response was not successful");
            }

            return response.getNyugta();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse Számlázz.hu XML", e);
        }
    }
}

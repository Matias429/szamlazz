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

            if (!response.getSikeres()) {
                System.out.println(response);
                throw new IllegalStateException(response.getHibauzenet());
            }

            if (response.getNyugtaPdf() != null) {
                response.getNyugta().getAlap().setNyugtaPdf(response.getNyugtaPdf());
            }
            return response.getNyugta();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
}

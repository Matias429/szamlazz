package hu.mbalazs.szamlazz.services;

import hu.mbalazs.szamlazz.dtos.PaymentItemsDto;
import hu.mbalazs.szamlazz.dtos.ReceiptItemsDto;
import hu.mbalazs.szamlazz.helpers.PaymentMethods;
import hu.mbalazs.szamlazz.helpers.XmlOutputProperties;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;

@Service
public class XmlGeneratorService {

    private final XmlOutputProperties xmlOutputProperties;

    public XmlGeneratorService(XmlOutputProperties xmlOutputProperties) {
        this.xmlOutputProperties = xmlOutputProperties;
    }

    public String parseDataToXml(String hivasazonosito, Boolean pdfLetoltes, String elotag, PaymentMethods fizmod, String penznem,
                                 Optional<String> megjegyes, List<ReceiptItemsDto.ReceiptItemDto> tetelek, Optional<List<PaymentItemsDto.PaymentItemDto>> kifizetesek) {
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .newDocument();

            // Root element
            Element nyugtaCreate = document.createElement("xmlnyugtacreate");
            document.appendChild(nyugtaCreate);
            nyugtaCreate.setAttribute("xmlns", "http://www.szamlazz.hu/xmlnyugtacreate");
            nyugtaCreate.setAttribute(("xmlns:xsi"), "http://www.w3.org/2001/XMLSchema-instance");
            nyugtaCreate.setAttribute(("xsi:schemaLocation"), "http://www.szamlazz.hu/xmlnyugtacreate http://www.szamlazz.hu/xmlnyugtacreate.xsd");

            // Beallitasok
            Element beallitasok = document.createElement("beallitasok");
            Element szamlaagentkulcsElement = document.createElement("szamlaagentkulcs");
            Element pdfLetoltesElement = document.createElement("pdfLetoltes");
            szamlaagentkulcsElement.setTextContent(xmlOutputProperties.getAgentId());
            pdfLetoltesElement.setTextContent(pdfLetoltes.toString());
            beallitasok.appendChild(szamlaagentkulcsElement);
            beallitasok.appendChild(pdfLetoltesElement);
            nyugtaCreate.appendChild(beallitasok);

            // Fejlec
            Element fejlec = document.createElement("fejlec");
            Element hivasAzonositoElement = document.createElement("hivasAzonosito");
            Element elotagElement = document.createElement("elotag");
            Element fizmodElement = document.createElement("fizmod");
            Element penznemElement = document.createElement("penznem");
            elotagElement.setTextContent(elotag);
            fizmodElement.setTextContent(fizmod.label);
            penznemElement.setTextContent(penznem);
            hivasAzonositoElement.setTextContent(hivasazonosito);
            fejlec.appendChild(hivasAzonositoElement);
            fejlec.appendChild(elotagElement);
            fejlec.appendChild(fizmodElement);
            fejlec.appendChild(penznemElement);
            if (megjegyes.isPresent()) {
                Element megjegyesElement = document.createElement("megjegyzes");
                megjegyesElement.setTextContent(megjegyes.get());
                fejlec.appendChild(megjegyesElement);
            }
            nyugtaCreate.appendChild(fejlec);

            // Tetelek
            Element tetelekElement = document.createElement("tetelek");
            for (ReceiptItemsDto.ReceiptItemDto item : tetelek) {
                Element tetel = document.createElement("tetel");

                Element megnevezes = document.createElement("megnevezes");
                megnevezes.setTextContent(item.getMegnevezes());
                tetel.appendChild(megnevezes);

                Element mennyiseg = document.createElement("mennyiseg");
                mennyiseg.setTextContent(item.getMennyiseg().toString());
                tetel.appendChild(mennyiseg);

                Element mennyisegiEgyseg = document.createElement("mennyisegiEgyseg");
                mennyisegiEgyseg.setTextContent(item.getMennyisegiEgyseg());
                tetel.appendChild(mennyisegiEgyseg);

                Element nettoEgysegar = document.createElement("nettoEgysegar");
                nettoEgysegar.setTextContent(item.getNettoEgysegar().toString());
                tetel.appendChild(nettoEgysegar);

                Element afakulcs = document.createElement("afakulcs");
                afakulcs.setTextContent(item.getAfakulcs());
                tetel.appendChild(afakulcs);

                Element netto = document.createElement("netto");
                netto.setTextContent(item.getNetto().toString());
                tetel.appendChild(netto);

                Element afa = document.createElement("afa");
                afa.setTextContent(item.getAfa().toString());
                tetel.appendChild(afa);

                Element brutto = document.createElement("brutto");
                brutto.setTextContent(item.getBrutto().toString());
                tetel.appendChild(brutto);

                tetelekElement.appendChild(tetel);
            }
            nyugtaCreate.appendChild(tetelekElement);

            // Kifizetesek
            Element kifizetesekElement = document.createElement("kifizetesek");

            if (kifizetesek.isPresent()) {
                for (PaymentItemsDto.PaymentItemDto payment : kifizetesek.get()) {
                    Element kifizetes = document.createElement("kifizetes");

                    Element fizetoeszkoz = document.createElement("fizetoeszkoz");
                    fizetoeszkoz.setTextContent(payment.getFizetoeszkoz());
                    kifizetes.appendChild(fizetoeszkoz);

                    Element osszeg = document.createElement("osszeg");
                    osszeg.setTextContent(payment.getOsszeg().toString());
                    kifizetes.appendChild(osszeg);

                    kifizetesekElement.appendChild(kifizetes);
                }
            }

            nyugtaCreate.appendChild(kifizetesekElement);


            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StringWriter writer = new StringWriter();

            transformer.transform(
                    new DOMSource(document),
                    new StreamResult(writer)
            );

            return writer.toString();

        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate XML", e);
        }
    }
}
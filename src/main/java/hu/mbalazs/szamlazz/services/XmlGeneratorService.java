package hu.mbalazs.szamlazz.services;

import hu.mbalazs.szamlazz.dtos.PaymentItemsDto;
import hu.mbalazs.szamlazz.dtos.ReceiptItemsDto;
import org.springframework.beans.factory.annotation.Value;
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

    private final String agentId;

    public XmlGeneratorService(@Value("${xml.output.agent-id}") String agentId) {
        this.agentId = agentId;
    }

    public String parseDataToXml(String callId, Boolean pdfDownload, String prefix, String paymentMethod, String currency,
                                 Optional<String> note, List<ReceiptItemsDto.ReceiptItemDto> itemList, Optional<List<PaymentItemsDto.PaymentItemDto>> paymentList) {
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .newDocument();

            // Root element
            Element receiptCreateElement = document.createElement("xmlnyugtacreate");
            document.appendChild(receiptCreateElement);
            receiptCreateElement.setAttribute("xmlns", "http://www.szamlazz.hu/xmlnyugtacreate");
            receiptCreateElement.setAttribute(("xmlns:xsi"), "http://www.w3.org/2001/XMLSchema-instance");
            receiptCreateElement.setAttribute(("xsi:schemaLocation"), "http://www.szamlazz.hu/xmlnyugtacreate http://www.szamlazz.hu/xmlnyugtacreate.xsd");

            // Beallitasok
            Element settingsElement = document.createElement("beallitasok");
            Element accountAgentIdElement = document.createElement("szamlaagentkulcs");
            Element pdfDownloadElement = document.createElement("pdfLetoltes");
            accountAgentIdElement.setTextContent(this.agentId);
            pdfDownloadElement.setTextContent(pdfDownload.toString());
            settingsElement.appendChild(accountAgentIdElement);
            settingsElement.appendChild(pdfDownloadElement);
            receiptCreateElement.appendChild(settingsElement);

            // Fejlec
            Element headerElement = document.createElement("fejlec");
            Element callIdElement = document.createElement("hivasAzonosito");
            Element prefixElement = document.createElement("elotag");
            Element paymentMethodElement = document.createElement("fizmod");
            Element currencyElement = document.createElement("penznem");
            prefixElement.setTextContent(prefix);
            paymentMethodElement.setTextContent(paymentMethod);
            currencyElement.setTextContent(currency);
            callIdElement.setTextContent(callId);
            headerElement.appendChild(callIdElement);
            headerElement.appendChild(prefixElement);
            headerElement.appendChild(paymentMethodElement);
            headerElement.appendChild(currencyElement);
            if (note.isPresent()) {
                Element noteElement = document.createElement("megjegyzes");
                noteElement.setTextContent(note.get());
                headerElement.appendChild(noteElement);
            }
            receiptCreateElement.appendChild(headerElement);

            // Tetelek
            Element itemListElement = document.createElement("tetelek");
            for (ReceiptItemsDto.ReceiptItemDto item : itemList) {
                Element itemElement = document.createElement("tetel");

                Element nameElement = document.createElement("megnevezes");
                nameElement.setTextContent(item.getName());
                itemElement.appendChild(nameElement);

                Element amountElement = document.createElement("mennyiseg");
                amountElement.setTextContent(item.getAmount().toString());
                itemElement.appendChild(amountElement);

                Element unitOfMeasureElement = document.createElement("mennyisegiEgyseg");
                unitOfMeasureElement.setTextContent(item.getUnitOfMeasure());
                itemElement.appendChild(unitOfMeasureElement);

                Element netUnitPriceElement = document.createElement("nettoEgysegar");
                netUnitPriceElement.setTextContent(item.getNetUnitPrice().toString());
                itemElement.appendChild(netUnitPriceElement);

                Element vatRateElement = document.createElement("afakulcs");
                vatRateElement.setTextContent(item.getVatRate());
                itemElement.appendChild(vatRateElement);

                Element netElement = document.createElement("netto");
                netElement.setTextContent(item.getNet().toString());
                itemElement.appendChild(netElement);

                Element vatElement = document.createElement("afa");
                vatElement.setTextContent(item.getVat().toString());
                itemElement.appendChild(vatElement);

                Element grossElement = document.createElement("brutto");
                grossElement.setTextContent(item.getGross().toString());
                itemElement.appendChild(grossElement);

                itemListElement.appendChild(itemElement);
            }
            receiptCreateElement.appendChild(itemListElement);

            // Kifizetesek

            if (paymentList.isPresent()) {
                Element paymentListElement = document.createElement("kifizetesek");
                for (PaymentItemsDto.PaymentItemDto payment : paymentList.get()) {
                    Element paymentElement = document.createElement("kifizetes");

                    Element meansOfPaymentElement = document.createElement("fizetoeszkoz");
                    meansOfPaymentElement.setTextContent(payment.getMeansOfPayment());
                    paymentElement.appendChild(meansOfPaymentElement);

                    Element amountElement = document.createElement("osszeg");
                    amountElement.setTextContent(payment.getAmount().toString());
                    paymentElement.appendChild(amountElement);

                    paymentListElement.appendChild(paymentElement);
                }
                receiptCreateElement.appendChild(paymentListElement);
            }



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
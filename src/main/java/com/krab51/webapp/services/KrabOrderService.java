package com.krab51.webapp.services;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.krab51.webapp.domain.Client;
import com.krab51.webapp.domain.KrabOrder;
import com.krab51.webapp.domain.KrabReport;
import com.krab51.webapp.dto.KrabOrderDto;
import com.krab51.webapp.exceptions.BusinessException;
import com.krab51.webapp.mappers.ObjectMapper;
import com.krab51.webapp.repositories.KrabOrderRepository;
import com.krab51.webapp.repositories.KrabReportRepository;
import com.krab51.webapp.repositories.TrapRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.itextpdf.text.Element.ALIGN_CENTER;
import static com.itextpdf.text.Element.ALIGN_LEFT;
import static com.krab51.webapp.utilities.CountUtilites.numberAsWords;
import static com.krab51.webapp.utilities.CountUtilites.priceAsWords;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@Profile("default")
public class KrabOrderService {
    private final Logger logger = getLogger(KrabOrderService.class);
    protected final KrabOrderRepository krabOrderRepository;
    private final ObjectMapper objectMapper;

    private final Sort SORT = Sort.by(DESC, "id");
    private final TrapRepository trapRepository;
    private final KrabReportRepository krabReportRepository;

    @Autowired
    public KrabOrderService(KrabOrderRepository krabOrderRepository,
                            ObjectMapper objectMapper,
                            TrapRepository trapRepository,
                            KrabReportRepository krabReportRepository) {
        this.krabOrderRepository = krabOrderRepository;
        this.objectMapper = objectMapper;
        this.trapRepository = trapRepository;
        this.krabReportRepository = krabReportRepository;
    }

    public void save(KrabOrderDto krabOrderDto) {
        logger.info("Calling save at krab order service");

        if (krabOrderDto.beginDate != null && krabOrderDto.beginDate.toLocalDate().isBefore(krabOrderDto.regDate))
            throw new BusinessException("Order date can't be before of register date");

        krabOrderRepository.save(objectMapper.krabOrderDtoToKrabOrder(krabOrderDto));
    }

    public void deleteById(Long id) {
        logger.info("Calling deleteById at krab order service");

        krabOrderRepository.deleteById(id);
    }

    public Optional<KrabOrderDto> findById(Long id) {
        logger.info("Calling findById at krab order service");

        return krabOrderRepository.findById(id).map(objectMapper::krabOrderToKrabOrderDto);
    }

    public Page<KrabOrderDto> findAll(int page, int size) {
        logger.info("Calling findAll at krab order service");

        return krabOrderRepository.findAll(PageRequest.of(page, size, SORT)).map(objectMapper::krabOrderToKrabOrderDto);
    }

    public Optional<ByteArrayResource> orderForPrintById(Long id) {
        logger.info("Calling orderForPrintById at order endpoint with id {}", id);

        KrabOrder targetOrder = krabOrderRepository.findById(id).orElseGet(null);
        if (Objects.isNull(targetOrder)) return Optional.empty();

        Client targetClient = targetOrder.client;
        Set<String> trapNumbers = targetOrder.traps;
        if (trapNumbers.isEmpty()) {
            throw new BusinessException("Trap numbers needed!");
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Document document = new Document();
        List<String> text = new LinkedList<>();

        String companyName = targetOrder.company.companyName;
        String companyAddress = targetOrder.company.companyAddress;
        String fishingArea = targetOrder.company.fishingArea;
        String miningPermit = targetOrder.company.miningPermit;

        text.add("на лов (вылов) водных биоресурсов при оказании услуг по организации любительского рыболовства\n");
        text.add("Номер записи в промысловом журнале:    " + id + "\n");
        text.add("Выдана гражданину:    " + targetClient.lastName + "    " + targetClient.firstName + "    " + targetClient.middleName + " \n");
        text.add("Паспорт РФ:    " + targetClient.docNumber + "          " + "выдан:    " + targetClient.docDate.format(dateFormatter) + " \n");
        text.add(fishingArea + "\n");
        text.add(miningPermit + "\n");
        text.add("Объём:  " + targetOrder.enlistedCnt + " ( " + numberAsWords(targetOrder.enlistedCnt) + " ) шт.\n");
        text.add("Орудия лова:  Удебные орудия добычи (вылова) или донные ловушки, соответствующие Правилам рыболовства для Северного рыбохозяйственного бассейна \n");

        for (String trapNumber : trapNumbers) {
            text.add("Сетная ловушка (краболовка) №    " + trapNumber + "    Дата регистрации:    " + trapRepository.findByRegNumber(trapNumber).regDate.format(dateFormatter));
        }

        text.add("\n");
        text.add("Период действия путёвки:\n");

            text.add("С:    " + targetOrder.beginDate.format(dateTimeFormatter) + "    по:    " + targetOrder.beginDate.plusDays(targetOrder.expirationDaysNumber).format(dateTimeFormatter) + "\n");

        text.add("Стоимость:    " + targetOrder.price + "    ( " + priceAsWords(targetOrder.price) + " ) рублей (Всего оплачено)\n");
        text.add("        С Правилами рыболовства для Северного рыбохозяйственного бассейна и установленными условиями рыболовства на РЛУ (далее-Правила) я ознакомлен и обязуюсь выполнять\n");
        text.add("___________" + targetClient.lastName + " " + targetClient.firstName + " " + targetClient.middleName + "____________\n");
        text.add("        С границами Рыболовного участка №455 и Правилами учета орудий добычи (вылова) ВБР и ведения реестра сетных орудий добычи (вылова) ВБР я ознакомлен (регистрация сетных ловушек) и обязуюсь выполнять\n");
        text.add("________" + targetClient.lastName + " " + targetClient.firstName + " " + targetClient.middleName + "________\n");
        text.add("        Я подтверждаю, что в случае нарушения вышеуказанных Правил буду нести ответственность, установленную законодательством и обязуюсь компенсировать убытки Пользователю Рыболовного участка, возникшие по моей вине в результате несоблюдения установленных Правил.\n");
        text.add("________" + targetClient.lastName + " " + targetClient.firstName + " " + targetClient.middleName + "________\n");
        text.add("        Услуги, оказываемые пользователем гражданину, осуществляющему любительское рыболовство:\n" +
                "организационное и информационное обеспечение любительского рыболовства на РЛУ\n" +
                "ВНИМАНИЕ! Результаты лова (независимо от вылова) подлежат обязательной регистрации по месту приобретения в течение 24 часов.\n" +
                "Во время ловли камчатского краба и его транспортировки путевка должна находиться у лица, на которое выписана путевка.\n" +
                "Путевка действительна при предъявлении документа, удостоверяющего личность.\n");
        text.add("\n");
        text.add("Путевку выдал: " + "              " + "ФИО-Оператора               " + "               \n"); // TODO вписать оператора выдавшего путевку
        text.add("Дата выдачи:          " + targetOrder.regDate.format(dateFormatter) + "                                                                   М.П.");
        text.add("");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }

        document.open();
        BaseFont baseFont;

        try {
            baseFont = BaseFont.createFont("./src/main/resources/fonts/cyrfont.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Failure to create BaseFont for iText in method #orderForPrintById");
        }

        Font font12 = new Font(baseFont, 12);
        Font font14 = new Font(baseFont, 14);
        Font font18 = new Font(baseFont, 18);

        try {
            Paragraph paragraph1 = new Paragraph(new Phrase(companyName + "\n", font14));
            paragraph1.add(new Phrase(companyAddress + "\n", font14));
            paragraph1.add(new Phrase("ПУТЕВКА (ДОГОВОР) №    " + id + "\n", font14));
            paragraph1.setAlignment(ALIGN_CENTER);
            document.add(paragraph1);

            document.add(new Paragraph());
            for (String str : text) {
                document.add(new Phrase(str, font12));
            }

            document.newPage();

            Paragraph paragraph2 = new Paragraph();
            paragraph2.add(new Phrase("СОГЛАСИЕ НА ОБРАБОТКУ ПЕРСОНАЛЬНЫХ ДАННЫХ\n", font14));
            paragraph2.setAlignment(ALIGN_CENTER);
            document.add(paragraph2);

            Paragraph paragraph3 = new Paragraph();
            paragraph3.add(new Phrase("        Я, " + targetClient.lastName + " " + targetClient.firstName + " " + targetClient.middleName + "        паспорт: " + targetClient.docNumber + "\n", font12));
            paragraph3.add(new Phrase("\"выдан:    " + targetClient.docDate.format(dateFormatter) + " " + targetClient.docAuthority + " \n", font12));
            paragraph3.add(new Phrase("зарегистрированный по адресу: " + targetClient.registration + "\n", font12));
            paragraph3.add(new Phrase("даю согласие на обработку своих персональных данных компании " + targetOrder.company.companyName + "\n", font12));
            paragraph3.add(new Phrase(targetOrder.company.companyAddress + ", в порядке,\n", font12));
            paragraph3.add(new Phrase("установленно ФЗ от 27.07.2006 г. № 152 ФЗ \" О персональных данных\".\n", font12));
            paragraph3.add(new Phrase("        Настоящие согласия на обработку персональных данных действует с момента его предоставления\n", font12));
            paragraph3.add(new Phrase("его оператору и до " + LocalDate.now().plusMonths(3).format(dateFormatter) + " г. и может быть отозвано мной в любое время путем подачи оператору" + "\n", font12)); // TODO на какой срок выдавать согласие?
            paragraph3.add(new Phrase("заявления в простой письменной форме.\n", font12));
            paragraph3.add(new Phrase("подпись                        ( " + targetClient.lastName + " )                                                 " + LocalDate.now().format(dateFormatter) + " г.\n", font12));
            paragraph3.add(new Phrase("\n", font12));

            paragraph3.setAlignment(ALIGN_LEFT);
            document.add(paragraph3);

            Paragraph paragraph4 = new Paragraph();
            paragraph4.add(new Phrase("АКТ ОКАЗАНИЯ УСЛУГ\n", font14));
            paragraph4.setAlignment(ALIGN_CENTER);
            document.add(paragraph4);

            Paragraph paragraph5 = new Paragraph();
            paragraph5.add(new Phrase("        Услуги оказываются гражданину, осуществляющему любительское рыболовство:\n", font12));
            paragraph5.add(new Phrase("организационное и информационное обеспечение любительского рыболовства на рыболовном участке.\n", font12));
            paragraph5.add(new Phrase("        Вышеперечисленные услуги выполнены полностью и в срок.\n", font12));
            paragraph5.add(new Phrase("        Претензий к объёму, качеству и срокам оказания услуг не имею.\n", font12));
            paragraph5.add(new Phrase("\n", font12));
            paragraph5.add(new Phrase("Оператор:                        " + "    ФИО-Оператора" + "                Гражданин:                        ( " + targetClient.lastName + " )\n", font12)); //TODO запихнуть распечатывающего оператора
            paragraph5.add(new Phrase("\n", font12));
            paragraph5.setAlignment(ALIGN_LEFT);
            document.add(paragraph5);

            Paragraph paragraph6 = new Paragraph();
            paragraph6.add(new Phrase("Внимание!\n", font12));
            paragraph6.add(new Phrase("Результаты лова (независимо от вылова) подлежат регистрации.\n", font12));
            paragraph6.add(new Phrase("Во время ловли камчатского крабапутевка должна находиться у лица, на которое была выписана путевка.\n", font12));
            paragraph6.add(new Phrase("\n", font12));
            paragraph6.setAlignment(ALIGN_CENTER);
            document.add(paragraph6);

            PdfPTable miningResultTable = new PdfPTable(7);

            PdfPCell cell1 = new PdfPCell(new Phrase("Дата  вылова", font12));
            miningResultTable.addCell(cell1);
            PdfPCell cell2 = new PdfPCell(new Phrase("Время вылова", font12));
            miningResultTable.addCell(cell2);
            PdfPCell cell3 = new PdfPCell(new Phrase("Вид", font12));
            miningResultTable.addCell(cell3);
            PdfPCell cell4 = new PdfPCell(new Phrase("Вылов экз.", font12));
            miningResultTable.addCell(cell4);
            PdfPCell cell5 = new PdfPCell(new Phrase("Вылов кг.", font12));
            miningResultTable.addCell(cell5);
            PdfPCell cell6 = new PdfPCell(new Phrase("Отпущено экз.", font12));
            miningResultTable.addCell(cell6);
            PdfPCell cell7 = new PdfPCell(new Phrase("Подпись", font12));
            miningResultTable.addCell(cell7);

            PdfPCell blankCell = new PdfPCell();
            PdfPCell typeCell = new PdfPCell(new Phrase("Краб камчатский", font12));

            List<KrabReport> totalReportList = krabReportRepository.findByKrabOrderId(id);
            if (totalReportList.isEmpty()) {
                for (int i = 0; i < 3; i++) {
                    miningResultTable.addCell(blankCell);
                    miningResultTable.addCell(blankCell);
                    miningResultTable.addCell(typeCell);
                    miningResultTable.addCell(blankCell);
                    miningResultTable.addCell(blankCell);
                    miningResultTable.addCell(blankCell);
                    miningResultTable.addCell(blankCell);
                }
            } else {
                int j = totalReportList.size();
                totalReportList.sort(new SortReportById());

                for (int i = 0; i < j; i++) {
                    miningResultTable.addCell(totalReportList.get(i).endDate.format(dateFormatter));
                    miningResultTable.addCell(totalReportList.get(i).endDate.format(timeFormatter));
                    miningResultTable.addCell(typeCell);
                    miningResultTable.addCell(new Phrase(String.valueOf(totalReportList.get(i).actuallyCnt), font12));
                    miningResultTable.addCell(new Phrase(String.valueOf(totalReportList.get(i).actuallyKgs), font12));
                    miningResultTable.addCell(new Phrase(String.valueOf(totalReportList.get(i).releasedCnt), font12));
                    miningResultTable.addCell(blankCell);
                }
            }
            document.add(miningResultTable);

        } catch (DocumentException e) {
            throw new RuntimeException("Failure to add text payload into Document of iText in method #orderForPrintById");
        }

        document.close();

        return Optional.of(new ByteArrayResource(byteArrayOutputStream.toByteArray()));
    }

    public Optional<ByteArrayResource> birkaForPrintById(Long id) {
        logger.info("Calling birkaForPrintById at order endpoint with id {}", id);

        KrabOrder targetOrder = krabOrderRepository.findById(id).orElseGet(null);
        if (Objects.isNull(targetOrder)) return Optional.empty();

        Set<String> trapNumbers = targetOrder.traps;
        if (trapNumbers.isEmpty()) {
            throw new BusinessException("Trap numbers needed!");
        }

        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BaseFont baseFont;

        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }

        document.open();

        try {
            baseFont = BaseFont.createFont("./src/main/resources/fonts/cyrfont.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Failure to create BaseFont for iText in method #orderForPrintById");
        }

        Font font18 = new Font(baseFont, 18);

        try {
            Paragraph paragraph = new Paragraph();
            String[] trapNumbersArray = trapNumbers.toArray(new String[0]);
            String trapOwnerName;

            for (int i = 0; i < trapNumbers.size(); i++) {
                paragraph.add(new Phrase("ЛОВУШКА КРАБОВАЯ\n", font18));
                trapOwnerName = trapRepository.findByRegNumber(trapNumbersArray[i]).trapOwner.name;
//                if (Objects.isNull(trapOwnerName)) { // TODO убрать ?
//                    paragraph.add(new Phrase("Владелец:  " + "\n", font18));
//                } else {
                    paragraph.add(new Phrase("Владелец:  " + trapOwnerName + "\n", font18));
//                }
                paragraph.add(new Phrase("Характеристики:  \n", font18));
                paragraph.add(new Phrase("Диаметр нижнего кольца: - 1 метр\n", font18));
                paragraph.add(new Phrase("Диаметр верхнего кольца: - 0.75 метра\n", font18));
                paragraph.add(new Phrase("Высота: - 0.65 метра\n", font18));
                paragraph.add(new Phrase("Внутренний размер ячеи - 70 мм.\n", font18));
                paragraph.add(new Phrase("Регистрационный номер:  " + trapNumbersArray[i] + "\n", font18));
            }

            paragraph.setAlignment(ALIGN_CENTER);
            document.add(paragraph);
        } catch (DocumentException e) {
            throw new RuntimeException("Failure to add text payload into Document of iText in method #orderForPrintById");
        }

        document.close();

        return Optional.of(new ByteArrayResource(byteArrayOutputStream.toByteArray()));
    }

    private static class SortReportById implements Comparator<KrabReport> {
        // Used for sorting in ascending order of id
        public int compare(KrabReport a, KrabReport b) {
            return (int) (a.id - b.id);
        }
    }
}
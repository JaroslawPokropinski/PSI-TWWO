package psi.domain.document;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import psi.domain.educationaleffect.entity.EducationalEffectCategory;
import psi.domain.fieldofstudy.entity.FieldOfStudy;
import psi.domain.organisationalunit.entity.OrganisationalUnitType;
import psi.domain.simpleattribute.boundary.SimpleAttributeMapper;
import psi.domain.subjectcard.boundary.LiteratureMapper;
import psi.domain.subjectcard.entity.ProgramContent;
import psi.domain.subjectcard.entity.SubjectCard;
import psi.domain.subjectcard.entity.SubjectClasses;
import psi.domain.subjectcard.entity.SubjectClassesType;
import psi.infrastructure.exception.IllegalArgumentAppException;
import psi.infrastructure.exception.ThrowingConsumer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SubjectCardPdfDocumentGenerator implements DocumentGenerator<SubjectCard> {

    private static final String FILENAME_PATTERN = "{0} - {1}.pdf";
    private static final BaseFont baseFont = createFont();
    private static final Font titleFont = new Font(baseFont, 24, Font.BOLD, BaseColor.BLACK);
    private static final Font headingFont = new Font(baseFont, 16, Font.BOLD, BaseColor.BLACK);
    private static final Font labelFont = new Font(baseFont, 11, Font.BOLD, BaseColor.BLACK);
    private static final Font valueFont = new Font(baseFont, 11, Font.NORMAL, BaseColor.BLACK);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    private final MessageSource messageSource;
    private final SimpleAttributeMapper simpleAttributeMapper;
    private final LiteratureMapper literatureMapper;

    private static BaseFont createFont() {
        try {
            return BaseFont.createFont("font/times_unicode.ttf", BaseFont.CP1250, BaseFont.NOT_EMBEDDED);
        } catch (DocumentException|IOException e) {
            throw new IllegalArgumentAppException("Error during font creation", e);
        }
    }

    @Override
    public psi.domain.document.Document generateDocument(SubjectCard subjectCard) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();
            Chunk glue = new Chunk(new VerticalPositionMark());
            Paragraph p = new Paragraph(subjectCard.getSubjectCode());
            p.add(new Chunk(glue));
            p.add(dateFormat.format(new Date()));
            document.add(p);

            addTitle("subject_card.title", document);

            addHeading("subject_card.general_information", document);
            addLineSeparator(headingFont, document);
            addNewLine(document);
            addFacultyOrDepartment(subjectCard, document);
            addLabelWithStringValue("subject_card.subject_name", subjectCard.getSubjectName(), document);
            addLabelWithStringValue("subject_card.subject_name_eng", subjectCard.getSubjectNameInEnglish(), document);
            addLabelWithOptionalStringValue("subject_card.field_of_study", subjectCard.getMainFieldOfStudy().map(FieldOfStudy::getName), document);
            addLabelWithOptionalStringValue("subject_card.specialization", subjectCard.getSpecialization(), document);
            addLabelWithEnumValue("subject_card.studies_level", subjectCard.getStudiesLevel(), document);
            addLabelWithEnumValue("subject_card.studies_form", subjectCard.getStudiesForm(), document);
            addLabelWithEnumValue("subject_card.subject_type", subjectCard.getSubjectType(), document);
            addLabelWithStringValue("subject_card.subject_code", subjectCard.getSubjectCode(), document);
            addLabelWithBooleanValue("subject_card.group_of_courses", subjectCard.getIsGroupOfCourses(), document);

            addNewLine(document);
            addSubjectClassesInfoTable(subjectCard, document);

            addNewLine(document);
            addHeading("subject_card.prerequisites", document);
            addLineSeparator(headingFont, document);
            addNewLine(document);
            addList(simpleAttributeMapper.mapToStrings(subjectCard.getPrerequisites()), document);

            addNextPage(document);
            addHeading("subject_card.objectives", document);
            addLineSeparator(headingFont, document);
            addNewLine(document);
            addList(simpleAttributeMapper.mapToStrings(subjectCard.getSubjectObjectives()), document);

            Map<EducationalEffectCategory, List<String>> educationalEffectCodesByCategory = subjectCard.getEducationalEffectCodesByCategory();
            addNewLine(document);
            addHeading("subject_card.educational_effects", document);
            addLineSeparator(headingFont, document);
            addNewLine(document);
            if (educationalEffectCodesByCategory.get(EducationalEffectCategory.KNOWLEDGE) != null) {
                addLabelWithStringValue("subject_card.educational_effects.knowledge", "", document);
                addList(educationalEffectCodesByCategory.getOrDefault(EducationalEffectCategory.KNOWLEDGE, Collections.emptyList()), document);
                addNewLine(document);
            }
            if (educationalEffectCodesByCategory.get(EducationalEffectCategory.SKILLS) != null) {
                addLabelWithStringValue("subject_card.educational_effects.skills", "", document);
                addList(educationalEffectCodesByCategory.getOrDefault(EducationalEffectCategory.SKILLS, Collections.emptyList()), document);
                addNewLine(document);
            }
            if (educationalEffectCodesByCategory.get(EducationalEffectCategory.SOCIAL_COMPETENCES) != null) {
                addLabelWithStringValue("subject_card.educational_effects.social_competences", "", document);
                addList(educationalEffectCodesByCategory.getOrDefault(EducationalEffectCategory.SOCIAL_COMPETENCES, Collections.emptyList()), document);
            }

            addNewLine(document);
            addHeading("subject_card.programme_content", document);
            addLineSeparator(headingFont, document);
            addNewLine(document);
            addProgramContentTable(subjectCard, document);

            addNewLine(document);
            addHeading("subject_card.used_teaching_tools", document);
            addLineSeparator(headingFont, document);
            addNewLine(document);
            addList(simpleAttributeMapper.mapToStrings(subjectCard.getUsedTeachingTools()), document);

            addNewLine(document);
            addHeading("subject_card.primary_literature", document);
            addLineSeparator(headingFont, document);
            addNewLine(document);
            addList(literatureMapper.mapToPrimaryLiteratureStrings(subjectCard.getLiterature()), document);

            addNewLine(document);
            addHeading("subject_card.secondary_literature", document);
            addLineSeparator(headingFont, document);
            addNewLine(document);
            addList(literatureMapper.mapToSecondaryLiteratureStrings(subjectCard.getLiterature()), document);

            addNewLine(document);
            addHeading("subject_card.supervisor", document);
            addLineSeparator(headingFont, document);
            addNewLine(document);
            addLabelWithStringValue("subject_card.name", subjectCard.getSupervisor().getName() + " " + subjectCard.getSupervisor().getSurname(), document);
            addLabelWithStringValue("subject_card.phone", subjectCard.getSupervisor().getPhoneNumber(), document);
            addLabelWithStringValue("subject_card.email", subjectCard.getSupervisor().getEmail(), document);

            document.close();
        } catch (DocumentException e) {
            throw new IllegalArgumentAppException("Error during document generation", e);
        }
        return new psi.domain.document.Document(generateFilename(subjectCard), new InputStreamResource(new ByteArrayInputStream(out.toByteArray())));
    }

    private void addTitle(String label, Document document) throws DocumentException {
        Paragraph title = new Paragraph(getTranslatedLabel(label), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);
    }

    private String getTranslatedLabel(String label) {
        return messageSource.getMessage(label, null, LocaleContextHolder.getLocale());
    }

    private void addHeading(String label, Document document) throws DocumentException {
        Paragraph heading = new Paragraph(getTranslatedLabel(label), headingFont);
        heading.setAlignment(Element.ALIGN_LEFT);
        document.add(heading);
    }

    private void addLineSeparator(Font font, Document document) throws DocumentException {
        document.add(new LineSeparator(font));
    }

    private void addNewLine(Document document) throws DocumentException {
        document.add(Chunk.NEWLINE);
    }

    private void addNextPage(Document document) throws DocumentException {
        document.add(Chunk.NEXTPAGE);
    }

    private void addFacultyOrDepartment(SubjectCard subjectCard, Document document) {
        if (subjectCard.getOrganisationalUnit().getType() == OrganisationalUnitType.FACULTY) {
            addLabelWithStringValue("subject_card.faculty", subjectCard.getOrganisationalUnit().getName(), document);
        } else {
            addLabelWithStringValue("subject_card.department", subjectCard.getOrganisationalUnit().getName(), document);
        }
    }

    private void addLabelWithOptionalStringValue(String label, Optional<String> value, Document document) {
        value.ifPresent(v -> addLabelWithStringValue(label, v, document));
    }

    private void addLabelWithStringValue(String label, String value, Document document) {
        try {
            Paragraph paragraph = new Paragraph();
            paragraph.add(new Chunk(getTranslatedLabel(label) + ": ", labelFont));
            paragraph.add(new Chunk(value, valueFont));
            paragraph.setAlignment(Element.ALIGN_LEFT);
            document.add(paragraph);
        } catch (DocumentException exception) {
            throw new IllegalArgumentAppException("Error during document generation", exception);
        }
    }

    private <T extends Enum<T>> void addLabelWithEnumValue(String label, T value, Document document) {
        addLabelWithStringValue(label, getTranslatedLabel(getLabelForEnum(value, label)), document);
    }

    private <T extends Enum<T>> String getLabelForEnum(T value, String prefix) {
        return prefix + "." + value.name().toLowerCase();
    }

    private void addLabelWithBooleanValue(String label, boolean value, Document document) {
        addLabelWithStringValue(label, value ? getTranslatedLabel("yes") : getTranslatedLabel("no"), document);
    }

    private void addSubjectClassesInfoTable(SubjectCard subjectCard, Document document) throws DocumentException {
        Map<SubjectClassesType, SubjectClasses> subjectClassesByType = subjectCard.getSubjectClassesByType();
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        getHeaderCellsForSubjectClassesInfoTable().forEach(table::addCell);
        getSubjectClassesInfoTableRow(subjectClassesByType, getTranslatedLabel("subject_card.zzu_hours"), this::getZzuHours).forEach(table::addCell);
        getSubjectClassesInfoTableRow(subjectClassesByType, getTranslatedLabel("subject_card.cnps_hours"), this::getCnpsHours).forEach(table::addCell);
        getSubjectClassesInfoTableRow(subjectClassesByType, getTranslatedLabel("subject_card.crediting_form"), this::getCreditingForm).forEach(table::addCell);
        getSubjectClassesInfoTableRow(subjectClassesByType, getTranslatedLabel("subject_card.final_course"), this::getIsFinalCourse).forEach(table::addCell);
        getSubjectClassesInfoTableRow(subjectClassesByType, getTranslatedLabel("subject_card.ects_points"), this::getEctsPoints).forEach(table::addCell);
        getSubjectClassesInfoTableRow(subjectClassesByType, getTranslatedLabel("subject_card.practical_ects_points"), this::getPracticalEctsPoints).forEach(table::addCell);
        getSubjectClassesInfoTableRow(subjectClassesByType, getTranslatedLabel("subject_card.bu_ects_points"), this::getBuEctsPoints).forEach(table::addCell);
        document.add(table);
    }

    private String getLabelForSubjectClassesType(SubjectClassesType subjectClassesType) {
        return getTranslatedLabel(getLabelForEnum(subjectClassesType, "subject_card.subject_classes"));
    }

    private List<PdfPCell> getHeaderCellsForSubjectClassesInfoTable() {
        return List.of(
                getValueCell("", Element.ALIGN_CENTER, 3),
                getHeadingCellForSubjectClassesType(SubjectClassesType.LECTURE),
                getHeadingCellForSubjectClassesType(SubjectClassesType.CLASSES),
                getHeadingCellForSubjectClassesType(SubjectClassesType.LABORATORY),
                getHeadingCellForSubjectClassesType(SubjectClassesType.PROJECT),
                getHeadingCellForSubjectClassesType(SubjectClassesType.SEMINAR));
    }

    private PdfPCell getValueCell(String text, int horizontalAlignment, int colSpan) {
        return getCell(text, valueFont, horizontalAlignment, colSpan);
    }

    private PdfPCell getLabelCell(String text, int horizontalAlignment, int colSpan) {
        return getCell(text, labelFont, horizontalAlignment, colSpan);
    }

    private PdfPCell getCell(String text, Font font, int horizontalAlignment, int colSpan) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2);
        cell.setColspan(colSpan);
        return cell;
    }

    private PdfPCell getHeadingCellForSubjectClassesType(SubjectClassesType type) {
        return getLabelCell(getLabelForSubjectClassesType(type), Element.ALIGN_CENTER, 1);
    }

    private List<PdfPCell> getSubjectClassesInfoTableRow(Map<SubjectClassesType, SubjectClasses> subjectClassesByType, String label, Function<SubjectClasses, String> valueMapper) {
        PdfPCell labelCell = getLabelCell(label, Element.ALIGN_LEFT, 3);
        PdfPCell lectureValueCell = getValueCell(valueMapper.apply(subjectClassesByType.get(SubjectClassesType.LECTURE)), Element.ALIGN_CENTER, 1);
        PdfPCell classesValueCell = getValueCell(valueMapper.apply(subjectClassesByType.get(SubjectClassesType.CLASSES)), Element.ALIGN_CENTER, 1);
        PdfPCell laboratoryValueCell = getValueCell(valueMapper.apply(subjectClassesByType.get(SubjectClassesType.LABORATORY)), Element.ALIGN_CENTER, 1);
        PdfPCell projectValueCell = getValueCell(valueMapper.apply(subjectClassesByType.get(SubjectClassesType.PROJECT)), Element.ALIGN_CENTER, 1);
        PdfPCell seminarValueCell = getValueCell(valueMapper.apply(subjectClassesByType.get(SubjectClassesType.SEMINAR)), Element.ALIGN_CENTER, 1);
        return List.of(labelCell, lectureValueCell, classesValueCell, laboratoryValueCell, projectValueCell, seminarValueCell);
    }

    private String getZzuHours(SubjectClasses subjectClasses) {
        return getSubjectClassesProperty(subjectClasses, SubjectClasses::getZzuHours);
    }

    private <T> String getSubjectClassesProperty(SubjectClasses subjectClasses, Function<SubjectClasses, T> extractor) {
        return Optional.ofNullable(subjectClasses)
                .map(extractor)
                .map(T::toString)
                .orElse("");
    }

    private String getCnpsHours(SubjectClasses subjectClasses) {
        return getSubjectClassesProperty(subjectClasses, SubjectClasses::getCnpsHours);
    }

    private String getCreditingForm(SubjectClasses subjectClasses) {
        return getSubjectClassesProperty(subjectClasses, applyTranslation("subject_card.crediting_form", SubjectClasses::getCreditingForm));
    }

    private <T extends Enum<T>> Function<SubjectClasses, String> applyTranslation(String translationPrefix, Function<SubjectClasses, T> function) {
        return function.andThen(value -> getTranslatedLabel(getLabelForEnum(value, translationPrefix)));
    }

    private String getIsFinalCourse(SubjectClasses subjectClasses) {
        return getSubjectClassesProperty(subjectClasses, booleanToX(SubjectClasses::getIsFinalCourse));
    }

    private Function<SubjectClasses, String> booleanToX(Function<SubjectClasses, Boolean> function) {
        return function.andThen(value -> value ? "X" : "");
    }

    private String getEctsPoints(SubjectClasses subjectClasses) {
        return getSubjectClassesProperty(subjectClasses, SubjectClasses::getEctsPoints);
    }

    private String getPracticalEctsPoints(SubjectClasses subjectClasses) {
        return getSubjectClassesProperty(subjectClasses, SubjectClasses::getPracticalEctsPoints);
    }

    private String getBuEctsPoints(SubjectClasses subjectClasses) {
        return getSubjectClassesProperty(subjectClasses, SubjectClasses::getBuEctsPoints);
    }

    private void addList(List<String> elements, Document document) throws DocumentException {
        com.itextpdf.text.List list = new com.itextpdf.text.List(true);
        elements.forEach(list::add);
        document.add(list);
    }

    private void addProgramContentTable(SubjectCard subjectCard, Document document) {
        subjectCard.getSubjectClasses().stream()
                .map(this::getTableForSubjectClasses)
                .forEach(ThrowingConsumer.wrapper(document::add));
    }

    private PdfPTable getTableForSubjectClasses(SubjectClasses subjectClasses) {
        String subjectTypeLabel = getLabelForSubjectClassesType(subjectClasses.getSubjectClassesType());
        PdfPTable table = new PdfPTable(10);
        table.setWidthPercentage(100);
        table.addCell(getHeaderFullRowCell(subjectTypeLabel));
        getProgramContentTableRows(subjectTypeLabel, subjectClasses).forEach(table::addCell);
        getTotalHoursRow(subjectClasses).forEach(table::addCell);
        return table;
    }

    private PdfPCell getHeaderFullRowCell(String subjectTypeLabel) {
        return getLabelCell(subjectTypeLabel, Element.ALIGN_CENTER, 10);
    }

    private List<PdfPCell> getProgramContentTableRows(String subjectTypeLabel, SubjectClasses subjectClasses) {
        return subjectClasses.getProgram().stream()
                .sorted(Comparator.comparing(ProgramContent::getNumber))
                .map(programContent -> getProgramContentTableRow(subjectTypeLabel, programContent))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<PdfPCell> getProgramContentTableRow(String subjectTypeLabel, ProgramContent programContent) {
        return List.of(
                getNumberCell(subjectTypeLabel, programContent),
                getDescriptionCell(programContent),
                getHoursCell(programContent));
    }

    private PdfPCell getNumberCell(String subjectTypeLabel, ProgramContent programContent) {
        return getValueCell(subjectTypeLabel.substring(0, 3) + " " + (programContent.getNumber() + 1), Element.ALIGN_LEFT, 1);
    }

    private PdfPCell getDescriptionCell(ProgramContent programContent) {
        return getValueCell(programContent.getDescription(), Element.ALIGN_LEFT, 8);
    }

    private PdfPCell getHoursCell(ProgramContent programContent) {
        return getValueCell(doubleToString(programContent.getNumberOfHours()) + "h", Element.ALIGN_CENTER, 1);
    }

    private List<PdfPCell> getTotalHoursRow(SubjectClasses subjectClasses) {
        return List.of(
                getTotalHoursLabelCell(),
                getTotalHoursValueCell(subjectClasses));
    }

    private PdfPCell getTotalHoursLabelCell() {
        return getLabelCell(getTranslatedLabel("subject_card.total_hours"), Element.ALIGN_LEFT, 9);
    }

    private PdfPCell getTotalHoursValueCell(SubjectClasses subjectClasses) {
        return getValueCell(subjectClasses.getZzuHours() + "h", Element.ALIGN_CENTER, 1);
    }

    private String doubleToString(double d) {
        if(d == (long) d)
            return String.format("%d", (long) d);
        else
            return String.format("%s", d);
    }

    private String generateFilename(SubjectCard subjectCard) {
        return MessageFormat.format(FILENAME_PATTERN, subjectCard.getSubjectCode(), subjectCard.getSubjectName());
    }

}

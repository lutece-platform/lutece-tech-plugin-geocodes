package fr.paris.lutece.plugins.geocodes.csv;

import com.opencsv.bean.BeanField;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import fr.paris.lutece.portal.service.i18n.I18nService;

public class CustomMappingStrategy<T> extends ColumnPositionMappingStrategy<T> {
    public CustomMappingStrategy() {
    }

    public String[] generateHeader(final T bean) throws CsvRequiredFieldEmptyException {
        final int numColumns = this.getFieldMap().values().size();
        super.generateHeader(bean);
        final String[] header = new String[numColumns];

        for(int i = 0; i < numColumns; ++i) {
            final BeanField beanField = this.findField(i);
            final String columnHeaderName = this.extractHeaderName(beanField);
            header[i] = columnHeaderName;
        }

        return header;
    }

    private String extractHeaderName(final BeanField beanField) {
        if (beanField != null && beanField.getField() != null && beanField.getField().getDeclaredAnnotationsByType(CsvBindByName.class).length != 0) {
            final CsvBindByName bindByNameAnnotation = ((CsvBindByName[])beanField.getField().getDeclaredAnnotationsByType(CsvBindByName.class))[0];
            return I18nService.getLocalizedString( bindByNameAnnotation.column( ), I18nService.getDefaultLocale( ) ) ;
        } else {
            return "";
        }
    }
}

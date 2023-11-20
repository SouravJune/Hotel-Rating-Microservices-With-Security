package com.ms.user.id_generator;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.hibernate.type.spi.TypeConfiguration;

public class CustomIdGenerator extends SequenceStyleGenerator {

	@Serial
	private static final long serialVersionUID = -2210446236534353332L;

	public static final String DATE_FORMAT_PARAMETER = "dateFormat";
	public static final String DATE_FORMAT_DEFAULT = "%tY-%tm";

	public static final String NUMBER_FORMAT_PARAMETER = "numberFormat";
	public static final String NUMBER_FORMAT_DEFAULT = "%05d";

	public static final String DATE_NUMBER_SEPARATOR_PARAMETER = "dateNumberSeparator";
	public static final String DATE_NUMBER_SEPARATOR_DEFAULT = "_";

	private String format;

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {

		/* To get format as: Year-Month-Date_Id */
		/* Long generatedId = (Long) super.generate(session, object); 
		   String formattedId = String.format("%05d", generatedId); 
		   return String.format("%s_%s", LocalDate.now(), formattedId); */

		/* To get format as: Year-Month_Id */
		Long generatedId = (Long) super.generate(session, object);
		String formattedId = String.format("%05d", generatedId);
		return String.format("%tY-%tm_%s", LocalDate.now(), LocalDate.now(), formattedId);
	}

	@Override
	public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {

		super.configure(new TypeConfiguration().getBasicTypeRegistry().getRegisteredType(Long.class), params,
				serviceRegistry);

		String dateFormat = ConfigurationHelper
				.getString(DATE_FORMAT_PARAMETER, params, DATE_FORMAT_DEFAULT).replace("%", "%1");
		String numberFormat = ConfigurationHelper
				.getString(NUMBER_FORMAT_PARAMETER, params, NUMBER_FORMAT_DEFAULT).replace("%", "%2");
		String dateNumberSeparator = ConfigurationHelper
				.getString(DATE_NUMBER_SEPARATOR_PARAMETER, params,DATE_NUMBER_SEPARATOR_DEFAULT);

		this.format = dateFormat + dateNumberSeparator + numberFormat;

	}

}

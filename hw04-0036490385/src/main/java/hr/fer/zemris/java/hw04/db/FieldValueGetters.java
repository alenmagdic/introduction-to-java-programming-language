package hr.fer.zemris.java.hw04.db;

public class FieldValueGetters {
	public static final IFieldValueGetter FIRST_NAME;
	public static final IFieldValueGetter LAST_NAME;
	public static final IFieldValueGetter JMBAG;

	static {
		FIRST_NAME = record -> record.getFirstName();
		LAST_NAME = record -> record.getLastName();
		JMBAG = record -> record.getJmbag();
	}
}

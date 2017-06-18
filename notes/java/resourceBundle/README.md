import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

ResourceBundle errorMessages = ResourceBundle.getBundle("ValidationService", locale);
String message = defaultValue
try{
	errorMessages.getString(fieldError.getErrorCode())
}catch(MissingResourceException e){
	//do nothing.
}

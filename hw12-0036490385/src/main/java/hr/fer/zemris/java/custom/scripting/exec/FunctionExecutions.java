package hr.fer.zemris.java.custom.scripting.exec;

import java.text.DecimalFormat;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Klasa koja sadrži statičku metodu za izvođenje zadane funkcije iz skripte.
 * Podržane funkcije su:
 * sin,decfmt,dup,swap,setMimeType,paramGet,pparamGet,pparamSet,pparamDel,tparamGet,tparamSet,tparamDel.
 *
 * @author Alen Magdić
 *
 */
public class FunctionExecutions {

	/**
	 * Izvodi funkciju sa zadanim imenom, korištenjem podataka sa zadanog stoga
	 * i korištenjem zadanog konteksta.
	 *
	 * @param functionName
	 *            ime funkcije koju je potrebno izvesti
	 * @param stack
	 *            stog koji sadrži argumente
	 * @param context
	 *            kontekst zahtjeva
	 */
	public static void execute(String functionName, Stack<Object> stack, RequestContext context) {
		switch (functionName) {
		case "sin":
			executeSin(stack);
			return;
		case "decfmt":
			executeDecfmt(stack);
			return;
		case "dup":
			executeDup(stack);
			return;
		case "swap":
			executeSwap(stack);
			return;
		case "setMimeType":
			executeSetMimeType(stack, context);
			return;
		case "paramGet":
			executeParamGet(stack, context);
			return;
		case "pparamGet":
			executePParamGet(stack, context);
			return;
		case "pparamSet":
			executePParamSet(stack, context);
			return;
		case "pparamDel":
			executePParamDel(stack, context);
			return;
		case "tparamGet":
			executeTParamGet(stack, context);
			return;
		case "tparamSet":
			executeTParamSet(stack, context);
			return;
		case "tparamDel":
			executeTParamDel(stack, context);
			return;
		}

	}

	/**
	 * Izvodi funkciju sin(x).
	 *
	 * @param stack
	 *            stog koji sadrži argument funkcije
	 */
	private static void executeSin(Stack<Object> stack) {
		Object x = stack.pop();
		Double number = getAsDouble(x);
		if (number == null) {
			throw new IllegalArgumentException("Argument " + x + "is not a valid argument for function sin.");
		}
		stack.push(Math.sin(Math.toRadians(number)));
	}

	/**
	 * Iz zadanog objekta stvara {@link Double} objekt. Da bi stvaranje bilo
	 * moguće, zadani objekt mora biti ili objekt tipa {@link Integer} ili
	 * objekta tipa {@link Double} (pri čemu se samo izvodi cast) ili
	 * {@link String} čiji je sadržaj moguće parsirati u {@link Double}.
	 *
	 * @param x
	 *            objekt koji sadrži broj
	 * @return objekt {@link Double} nastao iz zadanog objekta
	 */
	private static Double getAsDouble(Object x) {
		if (x instanceof Integer) {
			return Double.valueOf(Integer.toString((Integer) x));
		} else if (x instanceof String) {
			try {
				return Double.valueOf((String) x);
			} catch (NumberFormatException ex) {
			}
		} else if (x instanceof Double) {
			return (Double) x;
		}
		return null;
	}

	/**
	 * Izvodi funkciju decfmt. Funkcija koristi dva parametra od kojih je prvi
	 * decimalni broj, a drugi je string koji predstavlja obrazac kojim je
	 * potrebno formatirati dani broj.
	 *
	 * @param stack
	 *            stog koji sadrži argumente funkcije
	 */
	private static void executeDecfmt(Stack<Object> stack) {
		Object f = stack.pop();
		if (!(f instanceof String)) {
			throw new IllegalArgumentException("Argument " + f + "is not a valid argument for function sin.");
		}
		DecimalFormat format;
		try {
			format = new DecimalFormat((String) f);
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException("Pattern " + f + "is not a valid pattern!");
		}

		Object x = stack.pop();
		Double number = getAsDouble(x);
		if (number == null) {
			throw new IllegalArgumentException("Argument " + x + "is not a valid argument for function decfmt.");
		}
		stack.push(format.format(number));
	}

	/**
	 * Izvodi funkciju dup. Funkcija dup duplicira podatak na vrhu stoga.
	 *
	 * @param stack
	 *            stog koji sadrži argumente
	 */
	private static void executeDup(Stack<Object> stack) {
		Object x = stack.pop();
		stack.push(x);
		stack.push(x);
	}

	/**
	 * Izvodi funkciju swap. Funkcija zamjenjuje poredak dvaju vršnih elemenata
	 * sa stoga.
	 *
	 * @param stack
	 *            stog koji sadrži argumente
	 */
	private static void executeSwap(Stack<Object> stack) {
		Object a = stack.pop();
		Object b = stack.pop();
		stack.push(a);
		stack.push(b);
	}

	/**
	 * Izvodi funkciju setMimeType.
	 *
	 * @param stack
	 *            stog koji sadrži argumente
	 * @param context
	 *            kontekst zahtjeva
	 */
	private static void executeSetMimeType(Stack<Object> stack, RequestContext context) {
		Object x = stack.pop();

		context.setMimeType(x.toString());
	}

	/**
	 * Izvodi funkciju paramGet. Funkcija paramGet dohvaća vrijednost parametra
	 * za zadanim imenom. Ako paramatar sa zadanim imenom ne postoji, vraća
	 * vrijednost drugog argumenta u kojem je zapisana defaultna vrijednost.
	 *
	 * @param stack
	 *            stog koji sadrži argumente
	 * @param context
	 *            kontekst zahtjeva
	 */
	private static void executeParamGet(Stack<Object> stack, RequestContext context) {
		executeParameterGet(stack, name -> context.getParameter(name));
	}

	/**
	 * Izvodu funkciju pparamGet. Funkcija pparamGet dohvaća vrijednost trajnog
	 * parametra za zadanim imenom. Ako paramatar sa zadanim imenom ne postoji,
	 * vraća vrijednost drugog argumenta u kojem je zapisana defaultna
	 * vrijednost.
	 *
	 * @param stack
	 *            stog koji sadrži argumente
	 * @param context
	 *            kontekst zahtjeva
	 */
	private static void executePParamGet(Stack<Object> stack, RequestContext context) {
		executeParameterGet(stack, name -> context.getPersistentParameter(name));
	}

	/**
	 * Izvodu funkciju pparamSet. Funkcija pparamSet postavlja vrijednost
	 * trajnog parametra sa zadanim imenom na zadanu vrijednost.
	 *
	 * @param stack
	 *            stog koji sadrži argumente
	 * @param context
	 *            kontekst zahtjeva
	 */
	private static void executePParamSet(Stack<Object> stack, RequestContext context) {
		executeParameterSet(stack, (name, value) -> context.setPersistentParameter(name, value));
	}

	/**
	 * Funkcija koja vrši dohvat parametara. Sami dohvat je enkapsuliran u
	 * obliku strategije koju zadaje pozivatelj, a metoda vrši dohvat argumenata
	 * sa stoga (prvi argument je ime parametra, a drugi defaultna vrijednost) i
	 * postavlja rezultat na stog.
	 *
	 * @param stack
	 *            stog koji sadrži argumente
	 * @param valueGetter
	 *            strategija za dohvat vrijednosti parametra sa zadanim imenom
	 */
	private static void executeParameterGet(Stack<Object> stack, Function<String, String> valueGetter) {
		Object defVal = stack.pop();
		Object name = stack.pop();

		String value = valueGetter.apply(name.toString());
		stack.push(value == null ? defVal.toString() : value.toString());
	}

	/**
	 * Funkcija koja vrši postavljanje parametara. Samo postavljanje je
	 * enkapsulirano u obliku strategije koju zadaje pozivatelj, a metoda vrši
	 * dohvat argumenata sa stoga.
	 *
	 * @param stack
	 *            stog koji sadrži argumente
	 * @param valueSetter
	 *            strategija za postavljanje zadane vrijednosti zadanog
	 *            parametra
	 */
	private static void executeParameterSet(Stack<Object> stack, BiConsumer<String, String> valueSetter) {
		Object name = stack.pop();
		Object value = stack.pop();
		valueSetter.accept(name.toString(), value.toString());
	}

	/**
	 * Izvodi funkciju pparamDel. Funkcija pparamDel briše zadani trajni
	 * parametar.
	 *
	 * @param stack
	 *            stog koji sadrži argumente
	 * @param context
	 *            kontekst zahtjeva
	 */
	private static void executePParamDel(Stack<Object> stack, RequestContext context) {
		executeParameterDeletion(stack, name -> context.removePersistentParameter(name));
	}

	/**
	 * Izvodi brisanje parametra. Postupak brisanja enkapsuliran je strategijom
	 * za brisanje.
	 *
	 * @param stack
	 *            stog koji sadrži argumente
	 * @param valueDeleter
	 *            strategija za brisanje zadanog parametra
	 */
	private static void executeParameterDeletion(Stack<Object> stack, Consumer<String> valueDeleter) {
		Object name = stack.pop();
		valueDeleter.accept(name.toString());
	}

	/**
	 * Izvodi funkciju tparamGet. Funkcija tparamGet dohvaća privremeni
	 * parametar sa zadanim imenom.
	 *
	 * @param stack
	 *            stog koji sadrži argumente
	 * @param context
	 *            kontekst zahtjeva
	 */
	private static void executeTParamGet(Stack<Object> stack, RequestContext context) {
		executeParameterGet(stack, name -> context.getTemporaryParameter(name));
	}

	/**
	 * Izvodi funkciju tparamSet. Funkcija tparamSet postavlja vrijednost
	 * zadanog parametra na zadanu vrijednost.
	 *
	 * @param stack
	 *            stog koji sadrži argumente
	 * @param context
	 *            kontekst zahtjeva
	 */
	private static void executeTParamSet(Stack<Object> stack, RequestContext context) {
		executeParameterSet(stack, (name, value) -> context.setTemporaryParameter(name, value));
	}

	/**
	 * Izvodi funkciju tparamDel. Funkcija tparamDel briše zadani privremeni
	 * parametar.
	 *
	 * @param stack
	 *            stog koji sadrži argumente
	 * @param context
	 *            kontekst zahtjeva
	 */
	private static void executeTParamDel(Stack<Object> stack, RequestContext context) {
		executeParameterDeletion(stack, name -> context.removeTemporaryParameter(name));
	}

}

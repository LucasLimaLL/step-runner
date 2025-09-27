package br.com.lucaslima.steprunner.commons;

import br.com.lucaslima.steprunner.application.domains.ResolutionContext;
import br.com.lucaslima.steprunner.application.ports.out.ExpressionEvaluatorPort;
import br.com.lucaslima.steprunner.application.ports.out.PlaceholderResolverPort;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public final class SimpleExpressionEvaluator implements ExpressionEvaluatorPort {

    private static final Pattern BIN_OP = Pattern.compile("^\\s*(.+?)\\s*(==|!=|>=|<=|>|<)\\s*(.+)\\s*$");
    private static final Pattern BOOL_LITERAL = Pattern.compile("^(?i:true|false)$");
    private static final Pattern NUM_LITERAL = Pattern.compile("^[+-]?\\d+(?:\\.\\d+)?$");
    private static final Pattern QUOTED = Pattern.compile("^'(.*)'$");

    @Override
    public boolean evaluate(String expression, ResolutionContext resolutionContext, PlaceholderResolverPort placeholderResolverPort) {

        if (expression == null || expression.isBlank()) {
            return false;
        }

        String expanded = placeholderResolverPort.resolveString(expression, resolutionContext);

        Matcher m = BIN_OP.matcher(expanded);
        if (!m.matches()) {
            String v = expanded.trim().toLowerCase();
            if (v.equals("true")) return true;
            if (v.equals("false")) return false;
            return !v.isEmpty();
        }

        String left = m.group(1).trim();
        String op = m.group(2).trim();
        String right = m.group(3).trim();

        Object lVal = coerce(left);
        Object rVal = coerce(right);

        int cmp = compare(lVal, rVal);

        return switch (op) {
            case "==" -> equalsValue(lVal, rVal);
            case "!=" -> !equalsValue(lVal, rVal);
            case ">" -> cmp > 0;
            case "<" -> cmp < 0;
            case ">=" -> cmp >= 0;
            case "<=" -> cmp <= 0;
            default -> false;
        };

    }

    private static Object coerce(String token) {
        String tokenTrimmed = token.trim();
        Matcher withQuotes = QUOTED.matcher(tokenTrimmed);
        if (withQuotes.matches()) {
            return withQuotes.group(1);
        }
        if (BOOL_LITERAL.matcher(tokenTrimmed).matches()) {
            return Boolean.parseBoolean(tokenTrimmed);
        }
        if (NUM_LITERAL.matcher(tokenTrimmed).matches()) {
            if (tokenTrimmed.contains(".")) return Double.parseDouble(tokenTrimmed);
            try {
                return Long.parseLong(tokenTrimmed);
            } catch (NumberFormatException e) {
                return Double.parseDouble(tokenTrimmed);
            }
        }
        return tokenTrimmed;
    }

    private static boolean equalsValue(Object a, Object b) {
        if (a instanceof Number && b instanceof Number) {
            return Double.compare(((Number) a).doubleValue(), ((Number) b).doubleValue()) == 0;
        }
        return Objects.equals(String.valueOf(a), String.valueOf(b));
    }

    private static int compare(Object a, Object b) {
        if (a instanceof Number && b instanceof Number) {
            return Double.compare(((Number) a).doubleValue(), ((Number) b).doubleValue());
        }
        String sa = String.valueOf(a);
        String sb = String.valueOf(b);
        return sa.compareTo(sb);
    }
}

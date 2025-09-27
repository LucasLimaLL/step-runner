package br.com.lucaslima.steprunner.commons;

import br.com.lucaslima.steprunner.application.domains.ResolutionContext;
import br.com.lucaslima.steprunner.application.ports.out.PlaceholderResolverPort;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public final class SimplePlaceholderResolver implements PlaceholderResolverPort {

    private static final Pattern PLACEHOLDER = Pattern.compile("\\$\\{([^}]+)}");
    private static final Pattern FUNC_SEGMENT = Pattern.compile("([a-zA-Z0-9_.$:-]+)(\\|[a-zA-Z]+(\\([^)]*\\))?)*");


    @Override
    public String resolveString(String expr, ResolutionContext resolutionContext) {

        return Optional
                .ofNullable(expr)
                .map(expression -> {
                    Matcher matcher = PLACEHOLDER.matcher(expression);
                    StringBuffer sb = new StringBuffer();
                    while (matcher.find()) {
                        String rawInside = matcher.group(1).trim();
                        String resolved = String.valueOf(applyFunctions(resolveKeyWithDefault(rawInside, resolutionContext), rawInside, resolutionContext));
                        matcher.appendReplacement(sb, Matcher.quoteReplacement(resolved == null ? "" : resolved));
                    }
                    matcher.appendTail(sb);
                    return sb.toString();
                }).orElse(null);

    }

    @Override
    public Object resolveObject(Object expression, ResolutionContext resolutionContext) {
        switch (expression) {
            case null -> {
                return null;
            }
            case String s -> {
                return resolveString(s, resolutionContext);
            }
            case Map<?, ?> map -> {
                Map<Object, Object> out = new LinkedHashMap<>();
                for (Map.Entry<?, ?> e : map.entrySet()) {
                    out.put(e.getKey(), resolveObject(e.getValue(), resolutionContext));
                }
                return out;
            }
            case List<?> list -> {
                List<Object> out = new ArrayList<>(list.size());
                for (Object v : list) out.add(resolveObject(v, resolutionContext));
                return out;
            }
            default -> {
            }
        }

        if (expression.getClass().isArray()) {
            int length = Array.getLength(expression);
            List<Object> out = new ArrayList<>(length);
            for (int i = 0; i < length; i++) out.add(resolveObject(Array.get(expression, i), resolutionContext));
            return out;
        }
        return expression;
    }

    private Object resolveKeyWithDefault(String raw, ResolutionContext resolutionContext) {
        String keyExpression = raw;
        String defaultValue = null;

        int colonTopLevel = findTopLevelColon(raw);
        if (colonTopLevel >= 0) {
            keyExpression = raw.substring(0, colonTopLevel).trim();
            defaultValue = raw.substring(colonTopLevel + 1).trim();
        }

        Object value = resolveKeyPathOrFunctionChain(keyExpression, resolutionContext);
        if (isNullOrBlank(value) && defaultValue != null) {
            return defaultValue;
        }
        return value;
    }

    private Object resolveKeyPathOrFunctionChain(String expr, ResolutionContext ctx) {
        if (!FUNC_SEGMENT.matcher(expr).matches()) {
            return resolveKeyPath(expr, ctx);
        }
        String[] parts = expr.split("\\|");
        Object current = resolveKeyPath(parts[0], ctx);
        for (int i = 1; i < parts.length; i++) {
            String f = parts[i].trim();
            current = applySingleFunction(current, f);
        }
        return current;
    }

    private Object resolveKeyPath(String path, ResolutionContext resolutionContext) {
        String p = path.trim();
        if (p.equals("$item")) return resolutionContext.getStepResults().get("$item");
        if (p.equals("$index")) return resolutionContext.getStepResults().get("$index");

        if (p.startsWith("mdc.")) {
            String k = p.substring(4);
            String v = MDC.get(k);
            if (v == null || v.isBlank()) {
                String gen = java.util.UUID.randomUUID().toString();
                MDC.put(k, gen);
                v = gen;
            }
            return v;
        }

        if (p.startsWith("workflowInput")) {
            return dig(resolutionContext.getStepResults().get("workflowInput"), p.substring("workflowInput".length()));
        }

        int dot = p.indexOf('.');
        if (dot < 0) {
            return resolutionContext.getStepResults().get(p);
        }
        String stepName = p.substring(0, dot);
        String remainder = p.substring(dot);
        Object stepObj = resolutionContext.getStepResults().get(stepName);
        return dig(stepObj, remainder);
    }

    private Object dig(Object base, String remainder) {
        if (base == null) return null;
        String r = remainder;
        if (r.startsWith(".")) r = r.substring(1);
        if (r.isEmpty()) return base;
        String[] tokens = r.split("\\.");
        Object cur = base;
        for (String t : tokens) {
            if (cur == null) return null;
            String key = t.trim();
            if (key.isEmpty()) continue;
            if (cur instanceof Map<?, ?> m) {
                cur = m.get(key);
            } else {
                try {
                    var f = cur.getClass().getDeclaredField(key);
                    f.setAccessible(true);
                    cur = f.get(cur);
                } catch (Exception ignore) {
                    return null;
                }
            }
        }
        return cur;
    }

    private static boolean isNullOrBlank(Object v) {
        if (v == null) return true;
        if (v instanceof String s) return s.isBlank();
        return false;
    }

    private static int findTopLevelColon(String raw) {
        int depth = 0;
        for (int i = 0; i < raw.length(); i++) {
            char c = raw.charAt(i);
            if (c == '(') depth++;
            else if (c == ')') depth = Math.max(0, depth - 1);
            else if (c == ':' && depth == 0) return i;
        }
        return -1;
    }

    private static Object applyFunctions(Object value, String rawInside, ResolutionContext ctx) {
        if (!rawInside.contains("|")) return value;
        String[] parts = rawInside.split("\\|", -1);
        Object cur = value;
        for (int i = 1; i < parts.length; i++) {
            cur = applySingleFunction(cur, parts[i].trim());
        }
        return cur;
    }

    private static Object applySingleFunction(Object current, String funcSpec) {
        if (current == null) return null;
        if (funcSpec.equalsIgnoreCase("trim")) {
            return String.valueOf(current).trim();
        }
        if (funcSpec.equalsIgnoreCase("uppercase")) {
            return String.valueOf(current).toUpperCase();
        }
        if (funcSpec.startsWith("substring(") && funcSpec.endsWith(")")) {
            String inside = funcSpec.substring("substring(".length(), funcSpec.length() - 1);
            String[] args = inside.split("\\s*,\\s*");
            String s = String.valueOf(current);
            try {
                if (args.length == 1) {
                    int begin = Integer.parseInt(args[0]);
                    return begin < 0 ? "" : (begin > s.length() ? "" : s.substring(begin));
                } else if (args.length == 2) {
                    int begin = Integer.parseInt(args[0]);
                    int end = Integer.parseInt(args[1]);
                    begin = Math.max(0, begin);
                    end = Math.min(s.length(), end);
                    if (begin >= end) return "";
                    return s.substring(begin, end);
                }
            } catch (NumberFormatException ignore) {
                return s;
            }
        }
        return current;
    }
}

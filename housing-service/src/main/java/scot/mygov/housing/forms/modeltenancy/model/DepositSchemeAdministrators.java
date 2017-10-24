package scot.mygov.housing.forms.modeltenancy.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import scot.mygov.UnavailableResourceException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class DepositSchemeAdministrators {

    private final Map<String, DepositSchemeAdministrator> depositScemeAdministrators;

    public DepositSchemeAdministrators() {
        // load the data
        InputStream in = DepositSchemeAdministrators.class.getResourceAsStream("/depositSchemeAdministrators.json");

        try {
            List<DepositSchemeAdministrator> administrators = new ObjectMapper()
                    .readValue(in, new TypeReference<List<DepositSchemeAdministrator>>(){});
            depositScemeAdministrators = administrators
                    .stream()
                    .collect(toMap(admin -> admin.getName().toLowerCase(), identity()));
        } catch (IOException e) {
            throw new UnavailableResourceException("Unable to load deposit scheme adminstrators", e);
        }
    }

    public DepositSchemeAdministrator forName(String name) {
        return depositScemeAdministrators.get(name.toLowerCase());
    }
}

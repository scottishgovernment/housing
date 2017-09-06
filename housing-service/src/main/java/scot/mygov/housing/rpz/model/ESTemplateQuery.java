package scot.mygov.housing.rpz.model;

import java.util.Map;

/**
 * Created by z441571 on 01/09/2017.
 *
 * * GET _search/template
 * {
 *      "inline": "{ \"query\": { \"match\": { \"text\": \"{{query_string}}\" }}}",
 *      "params": {
 *          "date": "yyyy-MM-dd",
 *          "postcode": "EH1 1EH",
 *          "uprn": "1234567"
 *      }
 * }
 *
 */
public class ESTemplateQuery {

    private String inline;
    private Map<String, String> params;

    public ESTemplateQuery(String inline, Map<String, String> params) {
        this.inline = inline;
        this.params = params;
    }

    public String getInline() {
        return inline;
    }

    public Map<String, String> getParams() {
        return params;
    }

}

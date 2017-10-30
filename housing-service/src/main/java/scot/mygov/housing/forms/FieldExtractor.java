package scot.mygov.housing.forms;

import scot.mygov.housing.forms.modeltenancy.model.ModelTenancy;

import java.util.Map;

public interface FieldExtractor <T >{

    Map<String, Object> extractFields(T model);
}

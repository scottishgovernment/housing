package scot.mygov.housing.forms;

import java.util.Map;

public interface FieldExtractor <T >{

    Map<String, Object> extractFields(T model);
}

import constructions.Variable;

import java.util.Map;

public interface SemanticAnalyzer {

    boolean areAllTypesCorrect(TreeNode parseTree);

    Map<String, Variable> getVariableMap();

}

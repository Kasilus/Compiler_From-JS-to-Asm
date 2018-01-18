package semantic;

import parser.TreeNode;

import java.util.Map;

public interface SemanticAnalyzer {

    boolean areAllTypesCorrect(TreeNode parseTree);

    Map<String, Variable> getVariableMap();

}

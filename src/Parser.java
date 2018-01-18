import java.util.ArrayList;
import java.util.List;

public interface Parser {

    public TreeNode parse(List<LexerToken> lexemeTable);

}

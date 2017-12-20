import java.util.LinkedList;
import java.util.List;

public class Parser {

    LexerToken currentToken;
    LinkedList<LexerToken> queue;


    public TreeNode parse(String s) {

        lexer.analise(s);

        List<LexerToken> lexemeTable = lexer.getLexemeTable();

        queue = new LinkedList<>(lexemeTable);

        currentToken = queue.pollFirst();
        TreeNode node = new TreeNode(TreeNode.Type.PROGRAM, null, statement(), null);


        return node;


    }

    private void error(String s, int positionf) {
        System.out.printf("%" +( position + 1) + "s", "^");
        System.out.println("\nPosition: "+ position + "\tSyntax error! " + s);
        System.exit(1);
    }

    private TreeNode statement() {

        TreeNode node = null;


        if (currentToken.getName().equals("LEFT BRACE")) {
            node = new TreeNode(TreeNode.Type.EMPTY, null, null);
            currentToken = queue.pollFirst();

            while (!currentToken.getName().equals("RIGHT BRACE")) {
                node = new TreeNode(TreeNode.Type.STATEMENT, null, node, statement());
            }
            currentToken = queue.pollFirst();
        } else if (currentToken.getName().equals("SEMICOLON")) {
            node = new TreeNode(TreeNode.Type.EMPTY, null, null);

        } else if (currentToken.getType() == TokenType.TYPE) {

            node = new TreeNode(TreeNode.Type.TYPE, currentToken.getValue(), type(), null);
            currentToken = queue.pollFirst();


        } else {
            node = new TreeNode(TreeNode.Type.EXPR, expr(), null);
            if (!currentToken.getValue().equals(";")) {
                error(" ';' expected", currentToken.getPosition());
            }
            currentToken = queue.pollFirst();
        }

        return node;
    }

    private TreeNode type() {

        TreeNode node = null;

        node = assign();

        while (currentToken.getValue().equals(",")) {

            node = new TreeNode(TreeNode.Type.COMMA, node, assign());

        }

        if (!currentToken.getValue().equals(";")){
            error("';' expected", currentToken.getPosition());
        }

        return node;
    }

    private TreeNode assign() {

        TreeNode node = null;
        currentToken = queue.pollFirst();

        if (currentToken.getType() != TokenType.VAR) {
            error("var expected", currentToken.getPosition());
        }

        TreeNode varName = new TreeNode(TreeNode.Type.VAR, currentToken.getValue(), null, null, currentToken.getPosition());
        currentToken = queue.pollFirst();

        if (currentToken.getName().equals("LEFT SQUARE BRACKET")) {

            node = new TreeNode(TreeNode.Type.ARRAY, null, null);
            node.setOp1(varName);
            currentToken = queue.pollFirst();
            node.setOp2(expr());
            if (!currentToken.getValue().equals("]")) {
                error("']' expected", currentToken.getPosition());
            }
            currentToken = queue.pollFirst();
        } else {
            node = varName;
        }

        if (currentToken.getName().equals("ASSIGN")){
            currentToken = queue.pollFirst();
            int positionBuf = currentToken.getPosition();
            node = new TreeNode(TreeNode.Type.SET, null, node, expr(), positionBuf);
        }


        return node;

    }

    private TreeNode expr() {

        TreeNode node = null;


        if (currentToken.getType() != TokenType.VAR) {
            return test();
        }

        node = test();

        //node.name == TreeNode.Type.VAR &&
        if (currentToken.getValue().equals("=")) {
            currentToken = queue.pollFirst();
            int positionBuf = currentToken.getPosition();
            node = new TreeNode(TreeNode.Type.SET, null, node, expr(), positionBuf);

        }

        return node;

    }

    private TreeNode test() {
        TreeNode node = sum();
        if (currentToken.getValue().equals("&&")) {
            currentToken = queue.pollFirst();
            int positionBuf = currentToken.getPosition();
            node = new TreeNode(TreeNode.Type.AND,null, node, test(), positionBuf);
        } else if (currentToken.getValue().equals("==")) {
            currentToken = queue.pollFirst();
            int positionBuf = currentToken.getPosition();
            node = new TreeNode(TreeNode.Type.EQU, null, node, test(), positionBuf);
        }
        return node;
    }

    private TreeNode term() {
        TreeNode node = null;
        if (currentToken.getValue().equals("--")) {
            int positionBuf = currentToken.getPosition();
            node = new TreeNode(TreeNode.Type.DEC, null, null, null, positionBuf);
            currentToken = queue.pollFirst();
            if (currentToken.getType().equals(TokenType.VAR)) {
                node.setOp1(new TreeNode(TreeNode.Type.VAR, currentToken.getValue(), null, null, currentToken.getPosition()));
                currentToken = queue.pollFirst();
            } else {
                error("Expected var after '--'", currentToken.getPosition());
            }
            return node;
        } else if (currentToken.getValue().equals("-") || currentToken.getValue().equals("+")) {
            node = new TreeNode(TreeNode.Type.UNARY, currentToken.getValue(), null, null);
            currentToken = queue.pollFirst();
            if (currentToken.getType().equals(TokenType.VAR)) {
                node.setOp1(new TreeNode(TreeNode.Type.VAR, currentToken.getValue(), null, null, currentToken.getPosition()));
                currentToken = queue.pollFirst();
            } else if (currentToken.getType().equals(TreeNode.Type.CONST)) {
                node.setOp1(new TreeNode(TreeNode.Type.CONST, currentToken.getValue(), null, null, currentToken.getPosition()));
                currentToken = queue.pollFirst();
            }
            return node;
        } else if (currentToken.getType() == TokenType.VAR) {

            TreeNode varName = new TreeNode(TreeNode.Type.VAR, currentToken.getValue(), null, null, currentToken.getPosition());
            currentToken = queue.pollFirst();

            if (currentToken.getValue().equals("[")) {
                node = new TreeNode(TreeNode.Type.ARRAYPOS, null, null, null, currentToken.getPosition());
                node.setOp1(varName);
                currentToken = queue.pollFirst();
                node.setOp2(expr());
                if (!currentToken.getValue().equals("]")) {
                    error("']' expected", currentToken.getPosition());
                }
                currentToken = queue.pollFirst();
                return node;
            } else {
                node = varName;
                return node;
            }

        } else if (currentToken.getType() == TokenType.CONSTANT) {
            node = new TreeNode(TreeNode.Type.CONST, currentToken.getValue(), null, null, currentToken.getPosition());
            currentToken = queue.pollFirst();
            return node;
        } else {
            return parenExpr();
        }
    }

    private TreeNode sum() {
        TreeNode node = term();
        TreeNode.Type kind;
        while (currentToken.getValue().equals("+") ||
                currentToken.getValue().equals("-") ||
                currentToken.getValue().equals("*") ||
                currentToken.getValue().equals("/")) {
            if (currentToken.getValue().equals("+")) {
                kind = TreeNode.Type.ADD;
            } else if (currentToken.getValue().equals("-")) {
                kind = TreeNode.Type.SUB;
            } else if (currentToken.getValue().equals("*")) {
                kind = TreeNode.Type.MUL;
            } else {
                kind = TreeNode.Type.DIV;
            }
            int positionBuf = currentToken.getPosition();
            currentToken = queue.pollFirst();
            node = new TreeNode(kind, null, node, term(), positionBuf);
        }
        return node;
    }

    private TreeNode parenExpr() {
        if (!currentToken.getValue().equals("(")) {
            error("'(' expected", currentToken.getPosition());
        }
        currentToken = queue.pollFirst();
        TreeNode node = expr();
        if (!currentToken.getValue().equals(")")) {
            error("')' expected", currentToken.getPosition());
        }
        currentToken = queue.pollFirst();
        return node;
    }

}

package bugs;

import static org.junit.Assert.*;

import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

import tree.Tree;


public class ParserTest {
    Parser parser;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testParser() {
        parser = new Parser("");
        parser = new Parser("2 + 2");
    }
    
    @Test
    public void testIsVarDeclaration(){
    	use("var");
    	try {
            assertFalse(parser.isVarDeclaration());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("var hello \n");
    	assertTrue(parser.isVarDeclaration());
    	assertStackTopEquals(createNode(tree("var", "hello")));
    	
    	use("var hello1 hello2 \n");
    	try {
            assertFalse(parser.isVarDeclaration());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	
    	use("var hello1, hello2 \n");
    	assertTrue(parser.isVarDeclaration());
    	assertStackTopEquals(createNode(tree("var", "hello1", "hello2" )));
    	
    	use("var hello1, hello2, hello3 \n");
    	assertTrue(parser.isVarDeclaration());
    	assertStackTopEquals(createNode(tree("var", "hello1", "hello2", "hello3" )));
    }
    
    @Test
    public void testIsTurnToAction(){
    	use("");
    	assertFalse(parser.isTurnToAction());
    	use("turnto");
    	try {
            assertFalse(parser.isTurnToAction());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("turnto xyz+3 \n");
    	assertTrue(parser.isTurnToAction());
    	assertStackTopEquals(createNode(tree("turnto", tree("+", "xyz", "3.0"))));
    }
    
    @Test
    public void testIsTurnAction(){
    	use("");
    	assertFalse(parser.isTurnAction());
    	use("turn");
    	try {
            assertFalse(parser.isTurnAction());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("turn xyz+3 \n");
    	assertTrue(parser.isTurnAction());
    	assertStackTopEquals(createNode(tree("turn", tree("+", "xyz", "3.0"))));
    }
    
    @Test
    public void testIsSwitchStatement(){
    	use("");
    	assertFalse(parser.isTurnAction());
    	use ("switch {");
    	try {
            assertFalse(parser.isSwitchStatement());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("switch { \n }");
    	try {
            assertFalse(parser.isSwitchStatement());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use ("switch { \n } \n");
    	assertTrue(parser.isSwitchStatement());
    	use("switch { \n case xyz+3 \n turn xyz+3 \n } \n");
    	assertTrue(parser.isSwitchStatement());
    	assertStackTopEquals(createNode(tree("switch", tree("case", tree("+", "xyz", "3.0"), tree("block", tree("turn", tree("+", "xyz", "3.0")))))));
    	use("switch { \n case xyz+3 \n turn xyz+3 \n case abc+2 \n turnto abc+2 \n } \n");
    	
    }
    
    @Test
    public void testIsReturnStatement(){
    	use("");
    	assertFalse(parser.isReturnStatement());
    	use("return");
    	try {
            assertFalse(parser.isReturnStatement());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("return x+y");
    	try {
            assertFalse(parser.isReturnStatement());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("return x+y \n");
    	assertTrue(parser.isReturnStatement());
    	assertStackTopEquals(createNode(tree("return", tree("+", "x", "y"))));
    }
    
    @Test
    public void testIsMoveToAction(){
    	use("");
    	assertFalse(parser.isMoveToAction());
    	use("moveto");
    	try {
            assertFalse(parser.isMoveToAction());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("moveto xyz+2 a+b");
    	try {
            assertFalse(parser.isMoveToAction());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("moveto xyz+2 a+b \n");
    	try {
            assertFalse(parser.isMoveToAction());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("moveto xyz+2 , a+b");
    	try {
            assertFalse(parser.isMoveToAction());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("moveto xyz + 2 , a + b \n");
    	assertTrue(parser.isMoveToAction());
    	assertStackTopEquals(createNode(tree("moveto", tree("+", "xyz", "2.0"), tree("+", "a", "b"))));
    }
    
    @Test
    public void testIsMoveAction(){
    	use("");
    	assertFalse(parser.isMoveAction());
    	use ("move a+b");
    	try {
            assertFalse(parser.isMoveAction());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("move a+b \n");
    	assertTrue(parser.isMoveAction());
    	assertStackTopEquals(createNode(tree("move", tree("+", "a", "b"))));
    }
    
    @Test
    public void testIsLoopStatement(){
    	use("");
    	assertFalse(parser.isLoopStatement());
    	use("loop");
    	try {
            assertFalse(parser.isLoopStatement());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("loop a+b");
    	try {
            assertFalse(parser.isLoopStatement());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("loop {\n move a+b \n moveto x+y , a+b \n} \n");
    	assertTrue(parser.isLoopStatement());
    	assertStackTopEquals(createNode(tree("loop", tree("block", tree("move", tree("+", "a", "b")), tree("moveto", tree("+", "x", "y"), tree("+", "a", "b"))))));
    	
    	use("loop {\n move a+b \n} \n");
    	assertTrue(parser.isLoopStatement());
    	assertStackTopEquals(createNode(tree("loop", tree("block", tree("move", tree("+", "a", "b"))))));
    	
    	use("loop move a+b");
    	try {
            assertFalse(parser.isLoopStatement());
            fail();
        }
    	catch (SyntaxException e) {
        }
    }
    
    @Test
    public void testIsLineAction(){
    	use("");
    	assertFalse(parser.isLineAction());
    	use("line");
    	try {
            assertFalse(parser.isLineAction());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("line a+b");
    	try {
            assertFalse(parser.isLineAction());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("line a+b, c+d");
    	try {
            assertFalse(parser.isLineAction());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("line a+b, c+d, e+f");
    	try {
            assertFalse(parser.isLineAction());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("line a+b, c+d, e+f g+h");
    	try {
            assertFalse(parser.isLineAction());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("line a + b, c + d, e+f, g+h");
    	try {
            assertFalse(parser.isLineAction());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("line a + b, c + d, e+f, g+h \n");
    	assertTrue(parser.isLineAction());
    	assertStackTopEquals(createNode(tree("line", tree("+", "a", "b"), tree("+", "c", "d"), tree("+", "e", "f"), tree("+", "g", "h"))));
    }
    
    @Test
    public void testIsInitializationBlock(){
    	use("");
    	assertFalse(parser.isInitializationBlock());
    	use("initially");
    	try {
            assertFalse(parser.isInitializationBlock());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("initially {\n move a+b \n moveto x+y , a+b \n} \n");
    	assertTrue(parser.isInitializationBlock());
    	assertStackTopEquals(createNode(tree("initially", tree("block", tree("move", tree("+", "a", "b")), tree("moveto", tree("+", "x", "y"), tree("+", "a", "b"))))));
    	
    	use("initially {\n move a+b \n} \n");
    	assertTrue(parser.isInitializationBlock());
    	assertStackTopEquals(createNode(tree("initially", tree("block", tree("move", tree("+", "a", "b"))))));
    }
    
    @Test
    public void testIsFunctionDefinition(){
    	use("");
    	assertFalse(parser.isFunctionDefinition());
    	use("define");
    	try {
            assertFalse(parser.isFunctionDefinition());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("define {\n move a+b \n} \n");
    	try {
            assertFalse(parser.isFunctionDefinition());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("define abc {\n move a+b \n} \n");
    	assertTrue(parser.isFunctionDefinition());
    	assertStackTopEquals(createNode(tree("function", "abc", "var", tree("block", tree("move", tree("+", "a", "b"))))));
    
    	use("define abc using a, b, c {\n move a+b \n moveto x+y , a+b \n} \n");
    	assertTrue(parser.isFunctionDefinition());
    	assertStackTopEquals(createNode(tree("function", "abc", tree("var", "a", "b", "c"), tree("block", tree("move", tree("+", "a", "b")), tree("moveto", tree("+", "x", "y"), tree("+", "a", "b"))))));
    }
    
    @Test
    public void testIsFunctionCall(){
    	use("");
    	assertFalse(parser.isFunctionCall());
    	use("abc");
    	try {
            assertFalse(parser.isFunctionCall());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("abc (a+b , c+d)");
    	assertTrue(parser.isFunctionCall());
    	assertStackTopEquals(createNode(tree("call", "abc", tree("var", tree("+", "a", "b"), tree("+", "c", "d")))));
    	use("abc (a+b , c+d, e+f)");
    	assertTrue(parser.isFunctionCall());
    	assertStackTopEquals(createNode(tree("call", "abc", tree("var", tree("+", "a", "b"), tree("+", "c", "d"), tree("+", "e", "f")))));
    	
    }
    
    @Test
    public void testIsExitIfStatement(){
    	use("");
    	assertFalse(parser.isExitIfStatement());
    	use("exit");
    	try {
            assertFalse(parser.isExitIfStatement());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("exit if");
    	try {
            assertFalse(parser.isExitIfStatement());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use ("exit if a+b \n");
    	assertTrue(parser.isExitIfStatement());
    	
    	use ("exit a+b \n");
    	try {
            assertFalse(parser.isExitIfStatement());
            fail();
        }
    	catch (SyntaxException e) {
        }	
    }
    
    @Test
    public void testIsDoStatement(){
    	use("");
    	assertFalse(parser.isDoStatement());
    	use("do");
    	try {
            assertFalse(parser.isDoStatement());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("do abc (a+b , c+d) \n");
    	assertTrue(parser.isDoStatement());
    	assertStackTopEquals(createNode(tree("call", "abc", tree("var", tree("+", "a", "b"), tree("+", "c", "d")))));
    	use("do abc (a+b , c+d, e+f) \n");
    	assertTrue(parser.isDoStatement());
    	assertStackTopEquals(createNode(tree("call", "abc", tree("var", tree("+", "a", "b"), tree("+", "c", "d"), tree("+", "e", "f")))));	
    }
    
    @Test
    public void testIsComparator(){
    	use("");
    	assertFalse(parser.isComparator());
    	use("!");
    	try {
            assertFalse(parser.isComparator());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("!=");
    	assertTrue(parser.isComparator());
    	assertStackTopEquals(createNode(tree("!=")));
    	use("=");
    	assertTrue(parser.isComparator());
    	assertStackTopEquals(createNode(tree("=")));
    	use("<=");
    	assertTrue(parser.isComparator());
    	assertStackTopEquals(createNode(tree("<=")));
    	use(">=");
    	assertTrue(parser.isComparator());
    	assertStackTopEquals(createNode(tree(">=")));
    	use("<");
    	assertTrue(parser.isComparator());
    	assertStackTopEquals(createNode(tree("<")));
    	use(">");
    	assertTrue(parser.isComparator());
    	assertStackTopEquals(createNode(tree(">")));
    }
    
    @Test
    public void testIsColorStatement(){
    	use("");
    	assertFalse(parser.isColorStatement());
    	use("color");
    	try {
            assertFalse(parser.isColorStatement());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("color abc");
    	try {
            assertFalse(parser.isColorStatement());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("color blue \n");
    	assertTrue(parser.isColorStatement());
    	assertStackTopEquals(createNode(tree("color", "blue")));
    	use("color switch \n");
    	assertTrue(parser.isColorStatement());
    	assertStackTopEquals(createNode(tree("color", "switch")));
    	
    }
    
    @Test
    public void testIsBugDefinition(){
    	use("");
    	assertFalse(parser.isBugDefinition());
    	use ("Bug test");
    	try {
            assertFalse(parser.isBugDefinition());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("Bug abc { \n move a+b \n} \n");
    	assertTrue(parser.isBugDefinition());
    	assertStackTopEquals(createNode(tree("Bug", "abc", "list", "initially", tree("block", tree("move", tree("+", "a", "b"))), "list")));
    	use("Bug test { \n var abc, def \n initially { \n move a+b \n} \n move xyz \n moveto test1, test2 \n define test3 { \n move a+b \n} \n} \n");
    	assertTrue(parser.isBugDefinition());
    	assertStackTopEquals(createNode(tree("Bug", "test", tree("list", tree("var", "abc", "def")), tree("initially", tree("block", tree("move", tree("+", "a", "b")))), tree("block", tree("move", "xyz"), tree("moveto", "test1", "test2")), tree("list", tree("function", "test3", "var", tree("block", tree("move", tree("+", "a", "b"))))))));
//    	System.out.println(parser.stack.peek());


    }
    
    @Test
    public void testIsBlock(){
    	use("");
    	assertFalse(parser.isBlock());
    	use("{ \n } \n");
    	assertTrue(parser.isBlock());
    	assertStackTopEquals(createNode(tree("block")));
    	use("{ \n move a+b \n } \n");
    	assertTrue(parser.isBlock());
    	assertStackTopEquals(createNode(tree("block", tree("move", tree("+", "a", "b")))));
    	use("{ \n move a+b \n move xyz \n } \n");
    	assertTrue(parser.isBlock());
    	assertStackTopEquals(createNode(tree("block", tree("move", tree("+", "a", "b")), tree("move", "xyz"))));
    }
    
    @Test
    public void testIsAssignmentStatement(){
    	use("");
    	assertFalse(parser.isAssignmentStatement());
    	use ("test");
    	try {
            assertFalse(parser.isAssignmentStatement());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use ("test =");
    	try {
            assertFalse(parser.isAssignmentStatement());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("test = 12 \n");
    	assertTrue(parser.isAssignmentStatement());
    	assertStackTopEquals(createNode(tree("assign", "test", "12.0")));
    	use("test = 12 + 9 <= a+b \n ");
    	assertTrue(parser.isAssignmentStatement());
    	assertStackTopEquals(createNode(tree("assign", "test", tree("<=", tree("+", "12.0", "9.0"), tree("+", "a", "b")))));    	
    }
    
    
    @Test
    public void testIsAllbugsCode(){
    	use("");
    	assertFalse(parser.isAllbugsCode());
    	use("Allbugs");
    	try {
            assertFalse(parser.isAllbugsCode());
            fail();
        }
    	catch (SyntaxException e) {
        }
    	use("Allbugs { \n } \n");
    	assertTrue(parser.isAllbugsCode());
    	assertStackTopEquals(createNode(tree("Allbugs", "list", "list")));
    	use("Allbugs { \n var test \n define abc {\n move a+b \n} \n } \n");
    	assertTrue(parser.isAllbugsCode());
    	assertStackTopEquals(createNode(tree("Allbugs", tree("list", tree("var", "test")), tree("list", tree("function", "abc", "var", tree("block", tree("move", tree("+", "a", "b"))))))));
//    	System.out.println(parser.stack.peek());
    }
    
    @Test
    public void testIsProgram(){
    	use("");
    	assertFalse(parser.isProgram());
    	use ("Allbugs { \n } \n Bug abc { \n move a+b \n} \n");
    	assertTrue(parser.isProgram());
//    	System.out.println(parser.stack.pop().getValue().value);
    	assertStackTopEquals(createNode(tree("program", tree("Allbugs", "list", "list"),tree("list", tree("Bug", "abc", "list", "initially", tree("block", tree("move", tree("+", "a", "b"))), "list")))));
    	use("Bug abc { \n move a+b \n} \n");
    	assertTrue(parser.isProgram());
//    	System.out.println(parser.stack.peek());
    	assertStackTopEquals(createNode(tree("program", tree("Allbugs", "list", "list"), tree("list", tree("Bug", "abc", "list", "initially", tree("block", tree("move", tree("+", "a", "b"))), "list")))));
    	use("Allbugs {\n" + 
		"    var abc\n" + 
		"   \n" + 
		"    define forward using n {\n" + 
		"        move n // random pointless comment \n" + 
		"        return -n\n" + 
		"    }\n" + 
		"    define abc123 {\n" + 
		"        abc = 123\n" + 
		"    }\n" + 
		"}\n" + 
		"\n" + 
		"Bug Sally {\n" + 
		"    var a, b, c\n" + 
		"    var x, y\n" + 
		"    \n" + 
		"    initially {\n" + 
		"        x = -50\n" + 
		"        color red\n" + 
		"        line 0, 0, 25.3, 100/3\n" + 
		"    }\n" + 
		"    \n" + 
		"    y = 2 + 3 * a - b / c\n" + 
		"    y = ((2+3)*a)-(b/c)\n" + 
		"    loop{\n" + 
		"        y = y / 2.0\n" + 
		"        exit if y<=0.5\n" + 
		"    }\n" + 
		"    switch {\n" + 
		"    }\n" + 
		"    switch {\n" + 
		"        case x < y\n" + 
		"            moveto 3, x+y\n" + 
		"            turn x-y\n" + 
		"        case a <= x < y = z !=a >= b > c\n" + 
		"            turnto -abc123() + forward(x)\n" + 
		"    }\n" + 
		"    do forward(a)\n" + 
		"}\n" + 
		"Bug henry {\n" + 
		"    x = Sally.x\n" + 
		"    y = -Sally.y + 100\n" + 
		"}\n");
    	assertTrue(parser.isProgram());
//    	System.out.println(parser.stack.peek());
    }

    @Test
    public void testIsExpression() {
        Tree<Token> expected;
        
        use("250");
        assertTrue(parser.isExpression());
        assertStackTopEquals(createNode("250.0"));
        
        use("hello");
        assertTrue(parser.isExpression());
        assertStackTopEquals(createNode("hello"));

        use("(xyz + 3)");
        assertTrue(parser.isExpression());
        assertStackTopEquals(tree("+", "xyz", "3.0"));

        use("a + b + c");
        assertTrue(parser.isExpression());
        assertStackTopEquals(tree("+", tree("+", "a", "b"), "c"));

        use("a * b * c");
        assertTrue(parser.isExpression());
        assertStackTopEquals(tree("*", tree("*", "a", "b"), "c"));

        use("3 * 12.5 - 7");
        assertTrue(parser.isExpression());
        assertStackTopEquals(tree("-", tree("*", "3.0", "12.5"), createNode("7.0")));

        use("12 * 5 - 3 * 4 / 6 + 8");
        assertTrue(parser.isExpression());
        expected = tree("+",
                      tree("-",
                         tree("*", "12.0", "5.0"),
                         tree("/",
                            tree("*", "3.0", "4.0"),
                            "6.0"
                           )
                        ),
                      "8.0"
                     );
        assertStackTopEquals(expected);
                     
        use("12 * ((5 - 3) * 4) / 6 + (8)");
        assertTrue(parser.isExpression());
        expected = tree("+",
                      tree("/",
                         tree("*",
                            "12.0",
                            tree("*",
                               tree("-","5.0","3.0"),
                               "4.0")),
                         "6.0"),
                      "8.0");
        assertStackTopEquals(expected);
        
        use("");
        assertFalse(parser.isExpression());
        
        use("#");
        assertFalse(parser.isExpression());

        try {
            use("17 +");
            assertFalse(parser.isExpression());
            fail();
        }
        catch (SyntaxException e) {
        }
        try {
            use("22 *");
            assertFalse(parser.isExpression());
            fail();
        }
        catch (SyntaxException e) {
        }
    }

    @Test
    public void testUnaryOperator() {       
        use("-250");
        assertTrue(parser.isExpression());
        assertStackTopEquals(tree("-", "250.0"));
        
        use("+250");
        assertTrue(parser.isExpression());
        assertStackTopEquals(tree("+", "250.0"));
        
        use("- hello");
        assertTrue(parser.isExpression());
        assertStackTopEquals(tree("-", "hello"));

        use("-(xyz + 3)");
        assertTrue(parser.isExpression());
        assertStackTopEquals(tree("-", tree("+", "xyz", "3.0")));

        use("(-xyz + 3)");
        assertTrue(parser.isExpression());
        assertStackTopEquals(tree("+", tree("-", "xyz"), "3.0"));

        use("+(-xyz + 3)");
        assertTrue(parser.isExpression());
        assertStackTopEquals(tree("+",
                                        tree("+",
                                                   tree("-", "xyz"), "3.0")));
    }

    @Test
    public void testIsTerm() {        
        use("12");
        assertTrue(parser.isTerm());
        assertStackTopEquals(createNode("12.0"));
        
        use("12.5");
        assertTrue(parser.isTerm());
        assertStackTopEquals(createNode("12.5"));

        use("3*12");
        assertTrue(parser.isTerm());
        assertStackTopEquals(tree("*", "3.0", "12.0"));

        use("x * y * z");
        assertTrue(parser.isTerm());
        assertStackTopEquals(tree("*", tree("*", "x", "y"), "z"));
        
        use("20 * 3 / 4");
        assertTrue(parser.isTerm());
        assertEquals(tree("/", tree("*", "20.0", "3.0"), createNode("4.0")),
                     stackTop());

        use("20 * 3 / 4 + 5");
        assertTrue(parser.isTerm());
        assertEquals(tree("/", tree("*", "20.0", "3.0"), "4.0"),
                     stackTop());
        followedBy(parser, "+ 5");
        
        use("");
        assertFalse(parser.isTerm());
        followedBy(parser, "");
        
        use("#");
        assertFalse(parser.isTerm());followedBy(parser, "#");

    }

    @Test
    public void testIsFactor() {
        use("12");
        assertTrue(parser.isFactor());
        assertStackTopEquals(createNode("12.0"));

        use("hello");
        assertTrue(parser.isFactor());
        assertStackTopEquals(createNode("hello"));
        
        use("(xyz + 3)");
        assertTrue(parser.isFactor());
        assertStackTopEquals(tree("+", "xyz", "3.0"));
        
        use("12 * 5");
        assertTrue(parser.isFactor());
        assertStackTopEquals(createNode("12.0"));
        followedBy(parser, "* 5.0");
        
        use("17 +");
        assertTrue(parser.isFactor());
        assertStackTopEquals(createNode("17.0"));
        followedBy(parser, "+");

        use("");
        assertFalse(parser.isFactor());
        followedBy(parser, "");
        
        use("#");
        assertFalse(parser.isFactor());
        followedBy(parser, "#");
    }

    @Test
    public void testIsFactor2() {
        use("hello.world");
        assertTrue(parser.isFactor());
        assertStackTopEquals(tree(".", "hello", "world"));
        
        use("foo(bar)");
        assertTrue(parser.isFactor());
        assertStackTopEquals(tree("call", "foo",
                                        tree("var", "bar")));
        
        use("foo(bar, baz)");
        assertTrue(parser.isFactor());
        assertStackTopEquals(tree("call", "foo",
                                        tree("var", "bar", "baz")));
        
        use("foo(2*(3+4))");
        assertTrue(parser.isFactor());
        assertStackTopEquals(tree("call", "foo",
                                 tree("var",
                                     tree("*", "2.0",
                                         tree("+", "3.0", "4.0")))));
    }

    @Test
    public void testIsAddOperator() {
        use("+ - + $");
        assertTrue(parser.isAddOperator());
        assertTrue(parser.isAddOperator());
        assertTrue(parser.isAddOperator());
        assertFalse(parser.isAddOperator());
        followedBy(parser, "$");
    }

    @Test
    public void testIsMultiplyOperator() {
        use("* / $");
        assertTrue(parser.isMultiplyOperator());
        assertTrue(parser.isMultiplyOperator());
        assertFalse(parser.isMultiplyOperator());
        followedBy(parser, "$");
    }

    @Test
    public void testNextToken() {
        use("12 12.5 bogus switch + \n");
        assertEquals(new Token(Token.Type.NUMBER, "12.0"), parser.nextToken());
        assertEquals(new Token(Token.Type.NUMBER, "12.5"), parser.nextToken());
        assertEquals(new Token(Token.Type.NAME, "bogus"), parser.nextToken());
        assertEquals(new Token(Token.Type.KEYWORD, "switch"), parser.nextToken());
        assertEquals(new Token(Token.Type.SYMBOL, "+"), parser.nextToken());
        assertEquals(new Token(Token.Type.EOL, "\n"), parser.nextToken());
        assertEquals(new Token(Token.Type.EOF, "EOF"), parser.nextToken());
    }
    
//  ----- "Helper" methods
    
    /**
     * Sets the <code>parser</code> instance to use the given string.
     * 
     * @param s The string to be parsed.
     */
    private void use(String s) {
        parser = new Parser(s);
    }
    
    /**
     * Returns the current top of the stack.
     *
     * @return The top of the stack.
     */
    private Object stackTop() {
        return parser.stack.peek();
    }
    
    /**
     * Tests whether the top element in the stack is correct.
     *
     * @return <code>true</code> if the top element of the stack is as expected.
     */
    private void assertStackTopEquals(Tree<Token> expected) {
        assertEquals(expected, stackTop());
    }
    
    /**
     * This method is given a String containing some or all of the
     * tokens that should yet be returned by the Tokenizer, and tests
     * whether the Tokenizer in fact has those Tokens. To succeed,
     * everything in the given String must still be in the Tokenizer,
     * but there may be additional (untested) Tokens to be returned.
     * This method is primarily to test whether Tokens are pushed
     * back appropriately.
     * @param parser TODO
     * @param expectedTokens The Tokens we expect to get from the Tokenizer.
     */
    private void followedBy(Parser parser, String expectedTokens) {
        int expectedType;
        int actualType;
        StreamTokenizer actual = parser.tokenizer;

        Reader reader = new StringReader(expectedTokens);
        StreamTokenizer expected = new StreamTokenizer(reader);

        try {
            while (true) {
                expectedType = expected.nextToken();
                if (expectedType == StreamTokenizer.TT_EOF) break;
                actualType = actual.nextToken();
                assertEquals(typeName(expectedType), typeName(actualType));
                if (actualType == StreamTokenizer.TT_WORD) {
                    assertEquals(expected.sval, actual.sval);
                }
                else if (actualType == StreamTokenizer.TT_NUMBER) {
                    assertEquals(expected.nval, actual.nval, 0.001);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String typeName(int type) {
        switch(type) {
            case StreamTokenizer.TT_EOF: return "EOF";
            case StreamTokenizer.TT_EOL: return "EOL";
            case StreamTokenizer.TT_WORD: return "WORD";
            case StreamTokenizer.TT_NUMBER: return "NUMBER";
            default: return "'" + (char)type + "'";
        }
    }
    
    /**
     * Returns a Tree node consisting of a single leaf; the
     * node will contain a Token with a String as its value. <br>
     * Given a Tree, return the same Tree.<br>
     * Given a Token, return a Tree with the Token as its value.<br>
     * Given a String, make it into a Token, return a Tree
     * with the Token as its value.
     * 
     * @param value A Tree, Token, or String from which to
              construct the Tree node.
     * @return A Tree leaf node containing a Token whose value
     *         is the parameter.
     */
    private Tree<Token> createNode(Object value) {
        if (value instanceof Tree) {
            return (Tree) value;
        }
        if (value instanceof Token) {
            return new Tree<Token>((Token) value);
        }
        else if (value instanceof String) {
            return new Tree<Token>(new Token((String) value));
        }
        assert false: "Illegal argument: tree(" + value + ")";
        return null; 
    }
    
    /**
     * Builds a Tree that can be compared with the one the
     * Parser produces. Any String or Token arguments will be
     * converted to Tree nodes containing Tokens.
     * 
     * @param op The String value to use in the Token in the root.
     * @param children The objects to be made into children.
     * @return The resultant Tree.
     */
    private Tree<Token> tree(String op, Object... children) {
        Tree<Token> tree = new Tree<Token>(new Token(op));
        for (int i = 0; i < children.length; i++) {
            tree.addChild(createNode(children[i]));
        }
        return tree;
    }
}
import org.hyperskill.hstest.dynamic.input.DynamicTestingMethod;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;
import org.hyperskill.hstest.testing.TestedProgram;


import java.util.Arrays;
import java.util.List;

public class FlashcardsTest extends StageTest<String> {
    
    //Test 1, in this case the user gets the correct definition, same as test 2
    @DynamicTestingMethod
    CheckResult test1() {
        TestedProgram main = new TestedProgram();
        String output;
        
        main.start();
        
        if (main.isFinished()) {
            return CheckResult.wrong("Your program should wait for user input");
        }
        
        main.execute("print()");
        main.execute("outputs text");
        output = main.execute("outputs text").toLowerCase();
        
        if (output.equals("") || output.matches("\\s+")) {
            return CheckResult.wrong("Your program does not provide feedback to the user");
        }
        
        if (!output.contains("right")) {
            return CheckResult.wrong("Your output should contain the word \"right\" if the user gets the definition");
        }
        
        return CheckResult.correct();
        
    }
    
    @DynamicTestingMethod
    CheckResult test2() {
        TestedProgram main = new TestedProgram();
        String output;
        
        main.start();
    
        if (main.isFinished()) {
            return CheckResult.wrong("Your program should wait for user input");
        }
        
        main.execute("Dog");
        main.execute("A barking animal");
        output = main.execute("A barking animal").toLowerCase();
        
        if (output.equals("") || output.matches("\\s+")) {
            return CheckResult.wrong("Your program does not provide feedback to the user");
        }
        
        if (!output.contains("right")) {
            return CheckResult.wrong("Your output should contain the word \"right\" if the user gets the definition");
        }
        
        return CheckResult.correct();
        
    }
    //Test 3, in this case the user fails the definition, same as test 4
    @DynamicTestingMethod
    CheckResult test3() {
        TestedProgram main = new TestedProgram();
        String output;
        
        main.start();
    
        if (main.isFinished()) {
            return CheckResult.wrong("Your program should wait for user input");
        }
        
        main.execute("Jetbrains");
        main.execute("A place for people who love to code");
        output = main.execute("A place for people who hate to code").toLowerCase();
    
        if (output.equals("") || output.matches("\\s+")) {
            return CheckResult.wrong("Your program does not provide feedback to the user");
        }
        
        if (!output.contains("wrong")) {
            return CheckResult.wrong("Your output should contain the word \"wrong\" if the user fails the definition");
        }
        
        return CheckResult.correct();
        
    }
    
    @DynamicTestingMethod
    CheckResult test4() {
        TestedProgram main = new TestedProgram();
        String output;
        
        main.start();
    
        if (main.isFinished()) {
            return CheckResult.wrong("Your program should wait for user input");
        }
        
        main.execute("Cat");
        main.execute("A barking animal");
        output = main.execute("A purring animal").toLowerCase();
        
        if (output.equals("") || output.matches("\\s+")) {
            return CheckResult.wrong("Your program does not provide feedback to the user");
        }
        
        if (!output.contains("wrong")) {
            return CheckResult.wrong("Your output should contain the word \"wrong\" if the user fails the definition");
        }
        
        return CheckResult.correct();
        
    }
    
}

package myLang;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import myLang.tokenizer.Token;
import myLang.tokenizer.Tokenizer;
import myLang.tokenizer.TokenizerException;
import myLang.parser.Parser;
import myLang.parser.ParseException;
import myLang.parser.Program;
import myLang.typechecker.Typechecker;
import myLang.typechecker.TypeErrorException;
import myLang.codegen.CodeGen;
import myLang.codegen.CodeGenException;

public class MyLang {

    public static void usage() {
        System.out.println("Takes:");
        System.out.println("-Input myLang file");
        System.out.println("-Output JavaScript file");
    }
    
    public static String readFileToString(final String fileName) throws IOException {
        return Files.readString(new File(fileName).toPath());
    }
    
    public static void main(String[] args) throws IOException,
                                                  TokenizerException,
                                                  ParseException,
                                                  TypeErrorException,
                                                  CodeGenException{
        if (args.length != 2) {
            usage();
        } else {
            final String input = readFileToString(args[0]);
            final Token[] tokens = Tokenizer.tokenize(input);
            final Program program = Parser.parseProgram(tokens);
            Typechecker.typecheckProgram(program);
            CodeGen.writeProgram(program, new File(args[1]));
        }
    }

}

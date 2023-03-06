import java.util.Arrays;
import java.util.Iterator;

public class Lexer implements Iterable<char[]> {
  public char[] content;

  public Lexer(char[] content) {
    this.content = content;
  }

  private void trim_left(){
    while( this.content.length > 0 && this.content[0] == ' ' ){
      this.content = Arrays.copyOfRange(this.content, 1, this.content.length );
    }
  }

  public char[] next_token(){
    trim_left();

    if (this.content.length <= 0){
      throw new Error("Array content length is Zero which should not happen! check how are your slicing the array");
    }

    if(Character.isLetter(this.content[0])){
      int index = 0;
      while(index < this.content.length && Character.isLetter(this.content[index])){
        index++;
      }
      char[] token = Arrays.copyOfRange(this.content,0,index);
      this.content = Arrays.copyOfRange(this.content,index,this.content.length);
      return token;
    }

    char[] token = Arrays.copyOfRange(this.content,0,1);
    this.content = Arrays.copyOfRange(this.content,1,this.content.length);
    return token;
  }
  
  public Iterator<char[]> iterator(){
    return new LexerIterator();
  }

  private class LexerIterator implements Iterator<char[]> {

    @Override
    public boolean hasNext() {
      return content.length > 0;
    }

    @Override
    public char[] next() {
      return next_token();
    }
    
  }
}

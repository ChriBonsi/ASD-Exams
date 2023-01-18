package it.unicam.cs.asdl2223.mp2;

/**
 * An object of this class is an actor that uses an ASDL2223Deque<Character> as
 * a Stack in order to check that a sequence containing the following
 * characters: '(', ')', '[', ']', '{', '}' in any order is a string of balanced
 * parentheses or not. The input is given as a String in which white spaces,
 * tabs and newlines are ignored.
 * <p>
 * Some examples:
 * <p>
 * - " (( [( {\t (\t) [ ] } ) \n ] ) ) " is a string o balanced parentheses - " " is a
 * string of balanced parentheses - "(([)])" is NOT a string of balanced
 * parentheses - "( { } " is NOT a string of balanced parentheses - "}(([]))" is
 * NOT a string of balanced parentheses - "( ( \n [(P)] \t ))" is NOT a string
 * of balanced parentheses
 *
 * @author Template: Luca Tesei, Implementation: Christian Bonsignore - christian.bonsignore@studenti.unicam.it
 */
public class BalancedParenthesesChecker {

    // The stack is to be used to check the balanced parentheses
    private ASDL2223Deque<Character> stack;

    /**
     * Create a new checker.
     */

    //Usare uno stack significa utilizzare solo i metodi pop(), push() e peek()
    public BalancedParenthesesChecker() {
        this.stack = new ASDL2223Deque<Character>();
    }

    /**
     * Check if a given string contains a balanced parentheses sequence of
     * characters '(', ')', '[', ']', '{', '}' by ignoring white spaces ' ',
     * tabs '\t' and newlines '\n'.
     *
     * @param s the string to check
     * @return true if s contains a balanced parentheses sequence, false
     * otherwise
     * @throws IllegalArgumentException if s contains at least a character
     *                                  different form:'(', ')', '[', ']',
     *                                  '{', '}', white space ' ', tab '\t'
     *                                  and newline '\n'
     */
    public boolean check(String s) {
        this.stack.clear();

        //Scorro la stringa carattere per carattere
        for (int i = 0; i < s.length(); i++) {
            Character current = s.charAt(i);
            if (!(isAllowed(current))) throw new IllegalArgumentException("This character is not allowed.");

            //Se il primo carattere che voglio aggiungere allo stack è una parentesi chiusa, ritorno direttamente false
            if (this.stack.size() == 0 && isClose(current)) return false;

            //Se è uno degli altri caratteri permessi non ho bisogno di aggiungerlo allo stack
            if (isOtherAllowed(current)) continue;

            //Aggiungo solo le parentesi aperte
            if (isOpen(current)) {
                this.stack.push(current);
            } else if (isClose(current)) {
                //Non ho bisogno di aggiungere le parentesi chiuse, mi
                //basta solo che siano dello stesso tipo dell'ultima
                //aggiunta così da "formare una coppia", quindi tolgo
                //la parentesi aperta della coppia
                if (this.areSameType(current, this.stack.peek())) {
                    this.stack.pop();
                } else return false;
            }
        }
        //Se alla fine ho uno stack vuoto, significa che ho "accoppiato" tutte le parentesi
        //e quindi l'espressione è bilanciata
        return this.stack.isEmpty();
    }

    //controllo sia uno dei caratteri permessi
    private boolean isAllowed(Character toCheck) {
        return isCurly(toCheck) || isRound(toCheck) || isSquare(toCheck) || isOtherAllowed(toCheck);
    }

    //controllo siano parentesi graffe
    private boolean isCurly(Character toCheck) {
        return toCheck.equals('{') || toCheck.equals('}');
    }

    //controllo siano parentesi quadre
    private boolean isSquare(Character toCheck) {
        return toCheck.equals('[') || toCheck.equals(']');
    }

    //controllo siano parentesi tonde
    private boolean isRound(Character toCheck) {
        return toCheck.equals('(') || toCheck.equals(')');
    }

    //controllo siano gli altri caratteri ammessi
    private boolean isOtherAllowed(Character toCheck) {
        return toCheck.equals(' ') || toCheck.equals('\t') || toCheck.equals('\n');
    }

    //controllo siano parentesi di apertura
    private boolean isOpen(Character toCheck) {
        return toCheck.equals('(') || toCheck.equals('[') || toCheck.equals('{');
    }

    //controllo siano parentesi di chiusura
    private boolean isClose(Character toCheck) {
        return toCheck.equals(')') || toCheck.equals(']') || toCheck.equals('}');
    }

    //controllo siano parentesi dello stesso tipo
    private boolean areSameType(Character first, Character second) {
        return (isCurly(first) && isCurly(second)) || (isSquare(first) && isSquare(second)) || (isRound(first) && isRound(second));
    }
}

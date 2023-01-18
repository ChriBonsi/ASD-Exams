package it.unicam.cs.asdl2223.mp3;

import java.util.List;
import java.util.ArrayList;

/**
 * Class that solves an instance of the El Mamun's Caravan problem using
 * dynamic programming.
 * <p>
 * Template: Daniele Marchei and Luca Tesei, Implementation: Christian Bonsignore christian.bonsignore@studenti.unicam.it
 */
public class ElMamunCaravanSolver {

    // the expression to analyse
    private final Expression expression;

    // table to collect the optimal solution for each sub-problem,
    // protected just for Junit Testing purposes
    protected Integer[][] table;

    // table to record the chosen optimal solution among the optimal solution of
    // the sub-problems, protected just for JUnit Testing purposes
    protected Integer[][] tracebackTable;

    // flag indicating that the problem has been solved at least once
    private boolean solved;

    /**
     * Create a solver for a specific expression.
     *
     * @param expression The expression to work on
     * @throws NullPointerException if the expression is null
     */
    public ElMamunCaravanSolver(Expression expression) {
        if (expression == null){
            throw new NullPointerException("Creazione di solver con expression null.");
        }

        this.expression = expression;
        this.table = new Integer[this.expression.size()][this.expression.size()];
        this.tracebackTable = new Integer[this.expression.size()][this.expression.size()];
        this.solved = false;
    }

    /**
     * Returns the expression that this solver analyse.
     *
     * @return the expression of this solver
     */
    public Expression getExpression() {
        return this.expression;
    }

    /**
     * Solve the problem on the expression of this solver by using a given
     * objective function.
     *
     * @param function The objective function to be used when deciding which
     *                 candidate to choose
     * @throws NullPointerException if the objective function is null
     */
    public void solve(ObjectiveFunction function) {
        if (function == null) {
            throw new NullPointerException("The objective function is null.");
        }

        //riempo la matrice solo con i digits dell'espressione
        for (int i = 0; i < this.expression.size(); i += 2) {
            this.table[i][i] = (Integer) this.expression.get(i).getValue();
        }

        //il primo for gestisce il livello in cui l'algoritmo sta lavorando
        for (int level = 0; level < this.expression.size() - 2; level += 2) {
            //questo invece l'avanzamento di i
            for (int i = 0; i < this.expression.size() - level - 2; i += 2) {
                int j = i + level + 2;

                //lista per salvarmi i valori calcolati
                List<Integer> temp = new ArrayList<>();

                //qui gestisco i movimenti di k
                for (int k = 0; i + k + 2 < j+1; k += 2) {

                    //i risultati delle due "partizioni"
                    Integer a = this.table[i][i + k];
                    Integer b = this.table[i + k + 2][j];

                    //il calcolo dipende dal segno che c'è
                    if (this.expression.get(i + k + 1).getValue().equals("*")) {
                        temp.add(a * b);
                    } else if (this.expression.get(i + k + 1).getValue().equals("+")) {
                        temp.add(a + b);
                    }
                }
                //salvo in matrice il risultato migliore deciso dalla funzione
                this.table[i][j] = function.getBest(temp);

                //salvo nella matrice di traceback l'indice della k
                this.tracebackTable[i][j] = function.getBestIndex(temp) * 2;
            }
        }

        // Algoritmo risolto
        this.solved = true;
    }

    /**
     * Returns the current optimal value for the expression of this solver. The
     * value corresponds to the one obtained after the last solving (which used
     * a particular objective function).
     *
     * @return the current optimal value
     * @throws IllegalStateException if the problem has never been solved
     */
    public int getOptimalSolution() {
        if (!solved) {
            throw new IllegalStateException("The problem has never been solved.");
        }
        //La soluzione migliore è sempre quella nell'angolo in alto a destra della matrice
        return table[0][table.length - 1];
    }

    /**
     * Returns an optimal parenthesization corresponding to an optimal solution
     * of the expression of this solver. The parenthesization corresponds to the
     * optimal value obtained after the last solving (which used a particular
     * objective function).
     * <p>
     * If the expression is just a digit then the parenthesization is the
     * expression itself. If the expression is not just a digit then the
     * parethesization is of the form "(<parenthesization>)". Examples: "1",
     * "(1+2)", "(1*(2+(3*4)))"
     *
     * @return the current optimal parenthesization for the expression of this
     * solver
     * @throws IllegalStateException if the problem has never been solved
     */
    public String getOptimalParenthesization() {
        if (!solved) {
            throw new IllegalStateException("The problem has never been solved.");
        }
        return traceBack(0, this.expression.size() - 1);
    }

    /**
     * Determines if the problem has been solved at least once.
     *
     * @return true if the problem has been solved at least once, false
     * otherwise.
     */
    public boolean isSolved() {
        return this.solved;
    }

    @Override
    public String toString() {
        return "ElMamunCaravanSolver for " + expression;
    }

    private String traceBack(int i, int j) {

        //se l'espressione è corta e non necessita di essere parentesizzata, stampo direttamente la  stringa
        if (i == j) return this.expression.get(i).toString();

        //stringa del segno dell'operazione
        String op = this.expression.get(i + this.tracebackTable[i][j] + 1).toString();

        //ricorsione
        return "(" + traceBack(i, i + this.tracebackTable[i][j]) + op + traceBack(i + this.tracebackTable[i][j] + 2, j) + ")";
    }
}

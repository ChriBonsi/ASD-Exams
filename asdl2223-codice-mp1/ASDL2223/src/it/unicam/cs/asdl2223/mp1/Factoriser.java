package it.unicam.cs.asdl2223.mp1;

/**
 * Un fattorizzatore è un agente che fattorizza un qualsiasi numero naturale nei
 * sui fattori primi.
 *
 * @author Luca Tesei (template)
 * Christian Bonsignore christian.bonsignore@studenti.unicam.it (implementazione)
 */
public class Factoriser {

    private Factor[] listOfFactors;

    /**
     * Fattorizza un numero restituendo la sequenza crescente dei suoi fattori
     * primi. La molteplicità di ogni fattore primo esprime quante volte il
     * fattore stesso divide il numero fattorizzato. Per convenzione non viene
     * mai restituito il fattore 1. Il minimo numero fattorizzabile è 1. In
     * questo caso viene restituito un array vuoto.
     *
     * @param n un numero intero da fattorizzare
     * @return un array contenente i fattori primi di n
     * @throws IllegalArgumentException se si chiede di fattorizzare un
     *                                  numero minore di 1.
     */
    public Factor[] getFactors(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Si sta cercando di fattorizzare un numero minore di 1.");
        }

        if (n == 1) return new Factor[]{};
        if (n == 2) return new Factor[]{new Factor(2,1)};

        // utilizzo un crivello fino alla radice di n per efficienza, poiché
        CrivelloDiEratostene factoriser = new CrivelloDiEratostene((int) Math.sqrt(n));
        // superata la radice il numero restante può solo essere primo
        listOfFactors = new Factor[(int) Math.sqrt(n)];
        int contPosition = 0;

        while (factoriser.hasNextPrime()) {
            int prime = factoriser.nextPrime();
            int mult = 0;
            while (n % prime == 0) { //finché n è multiplo di prime
                n = n / prime;       //dividi n per prime
                mult++;              //e aumenta la molteplicità di prime
            }
            if (mult > 0) {
                listOfFactors[contPosition++] = new Factor(prime, mult);
            }
        }
        if(n>1){
            listOfFactors[contPosition++] = new Factor(n, 1);
        }
        Factor[] result = new Factor[contPosition];
        System.arraycopy(listOfFactors, 0, result, 0, contPosition);

        return result;
    }
}

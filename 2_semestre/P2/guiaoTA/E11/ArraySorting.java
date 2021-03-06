import static java.lang.System.*;

public class ArraySorting
{
  static public void main(String[] args)
  {
    if (args.length != 1) {
      err.println("Test sorting algorithms for a N number array.");
      err.println("Usage: java -ea p83 <N>");
      exit(1);
    }

    int[] a;
    int n = Integer.valueOf(args[0]);

    a = randomArray(n, n/2);
    sequentialSort(a, 0, a.length);
    a = randomArray(n, n/2);
    bubbleSort(a, 0, a.length);
    a = randomArray(n, n/2);
    insertionSort(a, 0, a.length);
    a = randomArray(n, n/2);
    mergeSort(a, 0, a.length);

    out.println("Assertion checking disabled or no problem found!");
  }

  /**
   * Sequential sort ("greedy" variation of selection sort).
   * Increasing sorting of integer subarray a[start..end[
   * @param a      the array to be (partially) sorted.
   * @param start  index of the first element to sort.
   * @param end    index of the first element not to be sorted (the last element to sort is {@code end-1}).
   * Requires:   a!=null and 0 <= start <= end <= a.length
   * Ensures:  The elements a[k] with start <= k < end are sorted.  the remaining elements are not changed.
   */
  static void sequentialSort(int[] a, int start, int end) {
    assert a!=null;
    assert 0<=start && start<=end && end<=a.length;

    for (int i=start; i<end-1; i++) {   // for each element (except the last):
      for (int j=i+1; j<end; j++) {     // scan each following element
        if (a[j] < a[i])  {             // if in wrong order
          swap(a, i, j);                // swap them
        }
      }
    }

    assert isSorted(a, start, end);
  }

  /**
   * Bubble sort.
   */
  static void bubbleSort(int[] a, int start, int end) {
    assert a!=null;
    assert 0<=start && start<=end && end<=a.length;

    int f = end;
    boolean existsSwap;
    do {
      existsSwap = true;
      for (int i=start; i<f; i++) {
        if (a[i] > a[i+1]) {
          swap(a, i, i-1);
          existsSwap =false;
        }
      }
      f--;
    } while (f>start+1 && existsSwap);

    assert isSorted(a, start, end);
  }

  /**
   * Ordena????o por inser????o, variante com pesquisa bin??ria.
   * 
   * Para uma demonstra????o visual do algritmo, ver:
   * http://web.engr.oregonstate.edu/~minoura/cs162/javaProgs/sort/InsertSort.html
   */
  static void insertionSort(int[] a, int start, int end) {
    assert a!=null;
    assert 0<=start && start<=end && end<=a.length;

    int i;
    for (i=start; i<end-1; i++) {
      int v = a[i];    // pr??ximo valor a inserir
      insertInOrder(v, a, start, i);
    }

    assert isSorted(a, start, end);
  }

  /**
   * Merge sort
   */
  static void mergeSort(int[] a, int start, int end) {
    assert a!=null;
    assert 0<=start && start<=end && end<=a.length;

    int n = end-start;  // dimens??o do (sub)array a ordenar
    if (n > 1) {        // Se o array tem mais que um elemento, ent??o:
      int mid = start + n/2;     // acha o meio do array;
      mergeSort(a, start, mid);  // ordena a primeira metade;
      mergeSort(a, mid+1, end);  // ordena a segunda metade;
      merge(a, start, mid, end); // e funde as duas metades ordenadas.
    } // Se tem menos que 2 elementos, n??o ?? preciso fazer nada.

    assert isSorted(a, start, end);
  }

  /**
   * Funde duas partes ordenadas de um array.
   * @param a[start..mid[  A primeira parte ordenada.
   * @param a[mid..end[     A segunda parte ordenada.
   * No final, a[start..end[ tem os elementos das duas partes, ordenados.
   */
  static void merge(int[] a, int start, int mid, int end) {
    assert a!=null;
    assert 0<=start && start<=mid && mid<=end && end<=a.length;

    int n = end-start;
    int[] tmp = new int[n];  // Cria array tempor??rio para o resultado
    int i1=start, i2=mid;   // ??ndices para percorrer cada parte
    int k = 0;               // ??ndice para percorrer o resultado

    while (i1<mid && i2<end) { // Enquanto houver elementos em ambas as partes,
      if (a[i1]<a[i2]) {        // escolhe o menor e junta-o ao resultado.
        tmp[k] = a[i1];
      }
      else {
        tmp[k] = a[i2];
      }
    }

    // Copia o resultado para a[start..end[
    arraycopy(tmp, 0, a, start, n);
  }

  /**
   * Insere um valor num sub-agregado ordenado. (Com pesquisa bin??ria)
   * @param valor o que vai ser introduzido.
   * @param a[start..end[ sub-agregado j?? ordenado.
   * Exige: (end < a.length)
   * No in??cio: a[start..end[ tem de estar ordenado.
   * No final: a[start..end+1[ est?? ordenado e inclui o valor.
   * O elemento a[end] original ?? destru??do.
   **/
  static void insertInOrder(int valor, int[] a, int start, int end) {
    assert end < a.length;
    // Onde colocar o valor na lista a[start..end[?
    int k = binarySearch(valor, a, start, end);  // Resposta: em a[k]!
    arraycopy(a, k, a, k+1, end-k);  // arranja uma "vaga" em a[k]; 
    a[k] = valor;           // coloca o valor no s??tio.
  }


  /**
   * Search algorithms:
   */

  /**
   * Descobre ??ndice do primeiro elemento >= x num subarray ordenado.
   * Usa Pesquisa Bin??ria.
   * @param x  o valor a procurar.
   * @param a[start..end[ o subarray de valores em ordem crescente.
   * @return r==start  se  x <= a[start].
   * @return r (com start<r<end)  se  a[r-1] < x <= a[r]
   * @return r==end  se  a[end-1] < x.
   * O resultado r ?? a posi????o onde se encontra o valor x (se a[r]==x), e/ou
   * a posi????o onde poder?? ser inserido o valor x sem perturbar a ordena????o.
   */
  static int binarySearch(int x, int[] a, int start, int end) {
    int i = start; int j = end; int m;
    // [i..j] ?? o intervalo de resultados poss??veis (para j??: [start..end])
    while (i<j) {  // Enquanto o intervalo tem mais de um elemento:
      m = (i+j-1)/2;   // acha ponto m??dio;
      if (x > a[m])    // se x ?? maior que o elemento do meio,
        i = m+1;      //   reduz intervalo para [m+1..j[
      else             // sen??o,
        j = m;        //   reduz intervalo para [i..m[
    }
    assert i==j;
    return j;     // j ser?? o menor ind??ce tal que a[j] >= x
  }

  /**
   * Checks if a given subarray is sorted (increasing order).
   * @param a[start..end[  the subarray to chack.
   * @return true    if subarray is sorted.
   */
  static boolean isSorted(int [] a, int start, int end) {
    assert a!=null;
    assert 0<=start && start<=end && end<=a.length;

    boolean result = true;
    for (int k=start+1; result && k<end; k++) 
      result = (a[k-1]<=a[k]);
    return result;
  }

  /**
   * Creates an integer random array with numbers in the interval [0; max].
   * @param len length of the array.
   * @param max maximum random value.
   * @return the array
   */
  static int[] randomArray(int len, int max) {
    assert len >= 0;
    assert max >= 0;

    int[] result = new int[len];
    for (int i = 0; i < len; i++)
      result[i] = (int)(Math.random()*(max+1));
    return result;
  }

  /**
   * Swaps two elements of an integer array.
   * @param a the array
   * @param i index of an element to swap
   * @param j index of the other element to swap
   * {@code i},{@code j} must be valid indexes within array {@code a}
   */
  static void swap(int[] a, int i, int j) {
    int temp;
    temp = a[i];
    a[i] = a[j];
    a[j] = temp;
  }
}


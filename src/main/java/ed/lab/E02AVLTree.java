package ed.lab;


import java.util.Comparator;
import java.util.NoSuchElementException;

/** Ejercicio 2: Implementación de Árbol AVL genérico. */
public class E02AVLTree<T> {

    private static final class Node<T> {
        T value;
        Node<T> left, right;
        int height; // 1 para hoja
        int size;   // # nodos del subárbol

        Node(T v) {
            this.value = v;
            this.height = 1;
            this.size = 1;
        }
    }

    private final Comparator<T> cmp;
    private Node<T> root;

    /** Instancia un árbol AVL con el comparador indicado. */
    public E02AVLTree(Comparator<T> comparator) {
        if (comparator == null) throw new IllegalArgumentException("Comparator null");
        this.cmp = comparator;
    }

    /** Inserta value (ignora duplicados). */
    public void insert(T value) {
        if (value == null) throw new IllegalArgumentException("Valor null");
        root = insert(root, value);
    }

    /** Elimina value si existe. */
    public void delete(T value) {
        if (value == null) throw new IllegalArgumentException("Valor null");
        root = delete(root, value);
    }

    /** Busca y retorna el valor igual a 'value', o null si no existe. */
    public T search(T value) {
        if (value == null) return null;
        Node<T> curr = root;
        while (curr != null) {
            int c = cmp.compare(value, curr.value);
            if (c == 0) return curr.value;
            curr = (c < 0) ? curr.left : curr.right;
        }
        return null;
    }

    /** Altura del árbol (0 si vacío). */
    public int height() { return height(root); }

    /** Número de nodos en el árbol. */
    public int size() { return size(root); }

    // ---------- Internos ----------

    private Node<T> insert(Node<T> node, T value) {
        if (node == null) return new Node<>(value);
        int c = cmp.compare(value, node.value);
        if (c < 0) {
            node.left = insert(node.left, value);
        } else if (c > 0) {
            node.right = insert(node.right, value);
        } else {
            return node; // duplicado: no insertar
        }
        update(node);
        return rebalance(node);
    }

    private Node<T> delete(Node<T> node, T value) {
        if (node == null) return null;
        int c = cmp.compare(value, node.value);
        if (c < 0) {
            node.left = delete(node.left, value);
        } else if (c > 0) {
            node.right = delete(node.right, value);
        } else {
            // encontrado
            if (node.left == null || node.right == null) {
                node = (node.left != null) ? node.left : node.right;
            } else {
                // sucesor (mínimo del subárbol derecho)
                Node<T> succ = minNode(node.right);
                node.value = succ.value;
                node.right = delete(node.right, succ.value);
            }
        }
        if (node == null) return null; // si era hoja
        update(node);
        return rebalance(node);
    }

    private Node<T> minNode(Node<T> n) {
        while (n.left != null) n = n.left;
        return n;
    }

    private int height(Node<T> n) { return (n == null) ? 0 : n.height; }
    private int size(Node<T> n)   { return (n == null) ? 0 : n.size; }

    private void update(Node<T> n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
        n.size   = 1 + size(n.left) + size(n.right);
    }

    private int balanceFactor(Node<T> n) {
        return height(n.left) - height(n.right);
    }

    private Node<T> rebalance(Node<T> n) {
        int bf = balanceFactor(n);
        if (bf > 1) {
            if (balanceFactor(n.left) < 0) n.left = rotateLeft(n.left);  // LR
            return rotateRight(n); // LL
        } else if (bf < -1) {
            if (balanceFactor(n.right) > 0) n.right = rotateRight(n.right); // RL
            return rotateLeft(n); // RR
        }
        return n;
    }

    private Node<T> rotateRight(Node<T> y) {
        Node<T> x = y.left;
        Node<T> T2 = x.right;
        x.right = y;
        y.left = T2;
        update(y);
        update(x);
        return x;
    }

    private Node<T> rotateLeft(Node<T> x) {
        Node<T> y = x.right;
        Node<T> T2 = y.left;
        y.left = x;
        x.right = T2;
        update(x);
        update(y);
        return y;
    }

    // ---------- (Opcional) select(k): útil si alguna prueba lo requiere ----------
    /** Retorna el k-ésimo (1-indexado) en O(log n). */
    public T select(int k) {
        if (k <= 0 || k > size()) throw new IllegalArgumentException("k fuera de rango");
        Node<T> curr = root;
        while (curr != null) {
            int leftSize = (curr.left != null) ? curr.left.size : 0;
            int rank = leftSize + 1;
            if (k == rank) return curr.value;
            if (k < rank) curr = curr.left;
            else { k -= rank; curr = curr.right; }
        }
        throw new NoSuchElementException("No encontrado");
    }
}

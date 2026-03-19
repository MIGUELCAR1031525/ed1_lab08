package ed.lab;

import java.util.ArrayDeque;
import java.util.Deque;

public class E01KthSmallest {


    public int kthSmallest(TreeNode<Integer> root, int k) {
        if (root == null || k <= 0) {
            throw new IllegalArgumentException("Parámetros inválidos");
        }

        Deque<TreeNode<Integer>> stack = new ArrayDeque<>();
        TreeNode<Integer> curr = root;
        int count = 0;

        while (curr != null || !stack.isEmpty()) {
            // bajar por la izquierda
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;             // <-- si tu TreeNode usa otro nombre, ajústalo aquí
            }

            // visitar
            curr = stack.pop();
            count++;
            Integer value = curr.value;       // <-- si tu TreeNode usa 'val' o getValue(), ajústalo
            if (count == k) return value;

            // moverse a la derecha
            curr = curr.right;                // <-- ajustar si tu TreeNode usa otro nombre
        }

        throw new IllegalArgumentException("k excede el número de nodos del árbol");
    }
}
import java.util.LinkedList;
import java.util.Queue;

public class BinaryTree<T extends Comparable<T>> {
    private Node root = null;
    private LinkedList<T> answer;

    private class Node {
        T element;
        Node left;
        Node right;

        Node(T element) {
            this.element = element;
            left = null;
            right = null;
        }

        private void setLeft(Node left) {
            this.left = left;
        }

        private void setRight(Node right) {
            this.right = right;
        }

        private boolean hasLeft() {
            return left != null;
        }

        private boolean hasRight() {
            return right != null;
        }

        public void setElement(T element) {
            this.element = element;
        }
    }

    public BinaryTree() {}

    public BinaryTree(T root) {
        this.root = new Node(root);
    }

    public void addElement(T element) {
        root = addElement(root, element);
    }

    private Node addElement(Node currentNode, T element) {
        if (currentNode == null) return new Node(element);
        if (element.compareTo(currentNode.element) < 0) currentNode.setLeft(addElement(currentNode.left, element));
        else if (element.compareTo(currentNode.element) > 0) currentNode.setRight(addElement(currentNode.right, element));
        return currentNode;
    }

    public void deleteElement(T element) {
        root = deleteElement(root, element);
    }

    private Node deleteElement(Node currentNode, T element) {
        if (currentNode == null) return null;
        if (element.compareTo(currentNode.element) == 0) {
            if (!currentNode.hasLeft() && !currentNode.hasRight()) return null;
            if (!currentNode.hasRight()) return currentNode.left;
            if (!currentNode.hasLeft()) return currentNode.right;
            T smallestElement = findTheSmallestElement(currentNode.right);
            currentNode.setElement(smallestElement);
            currentNode.setRight(deleteElement(currentNode.right, smallestElement));
            return currentNode;
        }
        if (element.compareTo(currentNode.element) < 0) {
            currentNode.setLeft(deleteElement(currentNode.left, element));
            return currentNode;
        }
        currentNode.setRight(deleteElement(currentNode.right, element));
        return currentNode;
    }

    private T findTheSmallestElement(Node currentNode) {
        return currentNode.hasLeft() ? findTheSmallestElement(currentNode.left): currentNode.element;
    }

    public LinkedList<T> BFS() {
        if (root == null) {
            return null;
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        answer = new LinkedList<>();
        while (!queue.isEmpty()) {
            Node currentNode = queue.remove();
            answer.add(currentNode.element);
            if (!currentNode.hasRight()) {
                queue.add(currentNode.right);
            }
            if (!currentNode.hasLeft()) {
                queue.add(currentNode.left);
            }
        }
        return answer;
    }

    public LinkedList<T> DFS() {
        answer = new LinkedList<>();
        DFS(root);
        return answer;
    }

    private void DFS(Node currentNode) {
        if (currentNode != null) {
            DFS(currentNode.left);
            DFS(currentNode.right);
            answer.add(currentNode.element);
        }
    }
}

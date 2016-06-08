package dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vietlc on 6/5/16.
 */
public class DictTree<T> {
    public DictNode root;

    public DictTree(T data){
        this.root = new DictNode(data);
    }

    public class DictNode {
        public T data;
        public DictNode parent;
        public List<DictNode> children;

        public String token;

        public DictNode(T data) {
            this.data = data;
            this.parent = null;
            this.children = new ArrayList<DictNode>();
        }

        public DictNode addChild(T data) {
            DictNode node = new DictNode(data);
            node.parent = this;
            this.children.add(node);

            return node;
        }

        public Boolean equals(DictNode node) {
            if(!this.data.equals(node.data)) return false;
            else if(this.parent == null && node.parent == null) return true;
            else if(this.parent == null && node.parent != null) return false;
            else if(this.parent != null && node.parent == null) return false;
            else return this.parent.equals(node.parent);
        }

        public DictNode searchDirectChildren(T data, DictNode startNode) {
            for (int i = 0; i < this.children.size(); i++) {
                DictNode node = this.children.get(i);
                if (node.data.equals(data)) {
                    return node;
                }
            }

            return startNode;
        }

        public Boolean isTokenizable() {
            for (int i = 0; i < this.children.size(); i++) {
                DictNode node = this.children.get(i);
                if (node.data.equals((char) 0)) {
                    return true;
                }
            }

            return false;
        }

        public Boolean isEndMarkingNode() {
            return this.data.equals((char) 0);
        }

    }
}


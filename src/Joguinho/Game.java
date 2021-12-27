/*
 * Apesar de ser uma má prática, usei acentuações no decorrer dos textos do jogo.
 */
package Joguinho;

import javax.swing.JOptionPane;

public class Game {
    
    private Node node;
    /**
     * Public constructor
     * @return Game
     */
    public static Game newGame() {
        return new Game(buildFirstNode());
    }
    
    //Primeira pergunta
    public void firstQuestion() {
        int input = JOptionPane.showOptionDialog(
            null, 
            "Pense em um Animal...", 
            "Qual o animal?", 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.INFORMATION_MESSAGE, 
            null, null, null
        );

        if(input == JOptionPane.OK_OPTION)
            this.play();
    }
    
    //Método que inicia o jogo
    public void play() {
        this.play(this.node);
    }
    
//  Métodos privados -------------------------------------------------------------------------------------------------------------
    
    //Construtor
    private Game() {}

    //Construtor com nó
    private Game(Node node) {
        this.node = node;
    }
    
    /**
     * This method starts the game.
     * @param node 
     */
    private void play(Node node) {
        if (node.hasNextNode())
            this.play(this.question(node) ? node.getRightNode() : node.getLeftNode());
        else
            this.question(node);
    }
    
    
     // Método que faz a pergunta para o usuário; Retorna um booleano.
    private boolean question(Node node) {
        int awswer = JOptionPane.showConfirmDialog(null, node.getQuestion(), "Qual o animal?", JOptionPane.YES_NO_OPTION);

        if (awswer == JOptionPane.YES_OPTION) {
            if (!node.hasNextNode())
                finalMessage();
            
            return true;
        } else if (!node.hasNextNode())
            this.repositionNode(node);
        
        return false;
    }
    
    //Resposta final após o acerto da pergunta
    private void finalMessage() {
        JOptionPane.showMessageDialog(null, "Conseguimos ler a sua mente, viu só?!");
    }
    
    /**
     * This method prompts the user to enter their response.
     * @param actual_node
     * @return Node
     */
    //Solicita que o usuário responda; retorna Node
    private Node ask(Node actual_node) {
        String animal = "", action = "", previous_animal = actual_node.getAnimal();
                
        animal = JOptionPane.showInputDialog(null, "Qual foi o animal que você pensou? ");
        
        animal = animal != null ? animal.trim() : null;
        if (animal != null && animal.equals("")) {
            JOptionPane.showOptionDialog(
                null, 
                "Nos diga um animal para continuarmos"
                + " a brincadeira.", 
                "Erro!", 
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null, null, null
            );
            
            return this.ask(actual_node);
        } else if (animal != null) {
            action = JOptionPane.showInputDialog(null, String.format("O que um(a) %s é mas um(a) %s nao é? ___________", animal, previous_animal));
        
            action = action != null ? action.trim() : null;
            if (action != null && action.equals("")) {
                JOptionPane.showOptionDialog(
                    null, 
                    "Você deve informar o que o animal faz para continuar a brincadeira.", 
                    "Erro!", 
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null, null, null
                );

                return this.ask(actual_node);
            }
        }
            
        return animal != null && action != null ? this.buildNewNode(actual_node, animal, action) : new Node("");
    }
    
    //Método que pergunta ao usuário algo sobre o animal e repete a primeira pergunta do jogo logo em seguida
    private void repositionNode(Node actual_node) {
        Node node = this.ask(actual_node);
        if (!node.getQuestion().equals("")) { 
            Node parent = actual_node.getParent();

            actual_node.setParent(node);

            if (parent.getLeftNode() == actual_node)
                parent.setLeftNode(node);
            else
                parent.setRightNode(node);

            this.firstQuestion();
        }
    }
    
    //Método que cria novos nós dentro da cadeia de nós de perguntas; Retorna Node
    private Node buildNewNode(Node actual_node, String animal, String action) {
        Node node = new Node(String.format("O animal que você pensou %s?", action));
        Node right_node = new Node(String.format("O animal que você pensou é um(a) %s?", animal), animal);
        
        node.setParent(actual_node.getParent()).setRightNode(right_node).setLeftNode(actual_node);
        right_node.setParent(node);
        
        return node;
    }
    
    //Método para iniciar os nós de perguntas; Nó principal, o primeiro a aparecer
    private static Node buildFirstNode() {
        Node main_node = new Node("O animal que você pensou vive na água?");
        Node right_node = new Node("O animal que você pensou é um Tubarão?", "Tubarão");
        Node left_node = new Node("O animal que você pensou é um Macaco?", "Macaco");
        
        main_node.setRightNode(right_node).setLeftNode(left_node);
        right_node.setParent(main_node);
        left_node.setParent(main_node);
        
        return main_node;
    }
    
//  /PRIVATE METHODS ------------------------------------------------------------------------------------------------------------
}


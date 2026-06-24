public static void run(Path path){
        csvGraph = null;
        try {
            csvGraph = CsvSolver.getGraphData(path);;
            
        } catch (Exception e) {
            //Problema con el CSV
            System.out.println("Hay un problema con el CSV");
            e.printStackTrace();
        }

        List<Node> nodes = getNodes(csvGraph);
        
        for (Node node : nodes) {
            System.out.println(node);
        }
        
    }

    public static List<Node> getNodes(String[][] csvGraph) {
        Map<String, Node> nodeMap = new HashMap<>();
        Map<String, List<Edge>> adjacencyMap = new HashMap<>();

        for (int i = 1; i < csvGraph.length; i++) {
            String[] row = csvGraph[i];
            String sourceName = row[0];
            String targetName = row[1];
            double cost = Double.parseDouble(row[2]);

            nodeMap.putIfAbsent(sourceName, new Node(sourceName));
            nodeMap.putIfAbsent(targetName, new Node(targetName));

            Node targetNode = nodeMap.get(targetName);
            Edge edge = new Edge(targetNode, cost);

            adjacencyMap.putIfAbsent(sourceName, new ArrayList<>());
            adjacencyMap.get(sourceName).add(edge);
        }

        for (Map.Entry<String, List<Edge>> entry : adjacencyMap.entrySet()) {
            Node sourceNode = nodeMap.get(entry.getKey());
            List<Edge> edges = entry.getValue();
            sourceNode.adjacencies = edges.toArray(new Edge[0]);
        }

        return new ArrayList<>(nodeMap.values());
    }

Salida: 

Data: A
   CosteCamino: 0,000000
   Edges: [B (cost: 2.0), C (cost: 5.0), D (cost: 2.0)]
   Parent: null

   Data: B
   CosteCamino: 0,000000
   Edges: [G (cost: 3.0)]
   Parent: null

   Data: C
   CosteCamino: 0,000000
   Edges: [D (cost: 3.0), E (cost: 4.0), G (cost: 2.0)]
   Parent: null

   Data: D
   CosteCamino: 0,000000
   Edges: [E (cost: 4.0)]
   Parent: null

   Data: E
   CosteCamino: 0,000000
   Edges: []
   Parent: null

   Data: F
   CosteCamino: 0,000000
   Edges: []
   Parent: null

   Data: G
   CosteCamino: 0,000000
   Edges: [F (cost: 8.0)]
   Parent: null
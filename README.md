# ManetModel

![VisualGraph.png](https://github.com/eikeviehmann/ManetModel/blob/master/VisualGraph.png?raw=true)

## Klement:
- [x] RadioPropagationModel 
- [x] Units representing transmission rate
 
## Eike: 

###### Graph:
- [x] Generic edge weight

###### GraphGenerator:
- [x] ManetNetwok mode

###### Panel:
- [ ] Adjust scaling for different screen resolutions
- [ ] Show temp. results 
  - Show Node/Vertex + Link/Edge property info 
    - with terminal -> info vertex 0
    - on click listener for visualVertex/visualEdge


###### Discussion:
- Remove generic types (N,L,V,E) from Manet and maybe also from UndirectedWeightedGraph classes? 
  - Non-gernic approach leads to a lot of ugly type casts in code -> prefer generics 
- Flow deployment strategy regarding ACO
 - Link per Link which might be more tailored for ACO
 - Complete flow deployment


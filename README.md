# ManetModel

## Klement:
- [ ] WaveFormPropagationModel 
  - [x] AffectedLinks
  - [ ] Link capacity 
- [x] IdealPropagationModel
- [ ] Propagation model using Free-space path loss and Shannon–Hartley theorem for bitrate computation
 
 
## Eike: 
- [x] ConsoleApp
  - [x] TreeParser (builds tree based command set)
    - [x] XML export/import graphs
    - [x] export graph as an image
- [x] Adjust/Fix Generators (limit number of connections based on given playground)
- [x] Add multiple colored paths to VisualGraph (paint colored lines under edges with different sizes to enable overlapping paths)
  - [ ] Paint VisualPaths to vertices to emphasize VisualPaths
- [ ] Adjust scaling for different screen resolutions


## Ideas:
- Results as Plots in JFrame
- Units representing flow bitrate (mbit...)
- Visualization
  - Adjust playground from text input fields for graph generation
  - Add 3D support (Game Engine)
  
## Discussion:
- Utilization as property of Links
- Getter/setter for RadioOccupationModel
- Interface to grant access to vertexAdjacencies in WeightedUndirectedGraph



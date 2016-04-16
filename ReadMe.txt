Current TODOs/issues (not necessarily in order of importance)

More Urgent:

Less Urgent:
   
> Should build in a way to track the fitness at any given iteration so we can look at fitness trends
   over time for comparison's sake. This should be pretty easy.
   
> Because the factors never really seem to decrease for MN and aren't re-normalized, they just keep getting
   bigger. This might not be an issue, but we should consider finding some way to mitigate this just
   in case.
   
> Find a way to scale back factor potential values (maybe /10 every so many iterations?)

> Move the constraint-checking things (for ICPSO, not MICPSO) into the fitness function b/c they 
  are problem-specific
   
   
   
   
   
 Done: 
 > Currently, a lot of our I/O stuff is hard-coded and inflexible (e.g. it breaks down if you try to
   have a node with more than 2 values). We can't test on more interesting/complex examples until
   this is fixed. -- Done. The problem was that I was dealing with array size checking incorrectly. 
   The start/stop index + 2 or + 1 is not hardcoded to read in a set number of values, it just moves
   indexing past the value of interest and the comma. Updated the comments to make this clearer.
   
> We need some bigger test examples in a baaaaaaad way (or at least, we will very soon)-- Done. Added 
  12 examples of graph coloring problems, with sizes varying from 5 - 38 nodes. Also varied the examples
  to include different optimal colorings.     
  
  > Double check that all graph coloring examples are correct (in terms of nodes and edges), and that
  the values make sense.  - Done! All double checked, values changed to appropriate values, and pushed.
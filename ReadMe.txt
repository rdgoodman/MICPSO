Current TODOs/issues (not necessarily in order of importance)

More Urgent:
     
> PSO termination criterion is still untested

> Double check that all graph coloring examples are correct (in terms of nodes and edges), and that
  the values make sense. 



Less Urgent:
   
> We'll probably need some error/exception-checking to make sure probabilities stay within
   valid boundaries (none < 0 or > 1)
   
> Should build in a way to track the fitness at any given iteration so we can look at fitness trends
   over time for comparison's sake. This should be pretty easy.
   
> Because the factors never really seem to decrease for MN and aren't re-normalized, they just keep getting
   bigger. This might not be an issue, but we should consider finding some way to mitigate this just
   in case.
   
   
   
   
   
 Done: 
 > Currently, a lot of our I/O stuff is hard-coded and inflexible (e.g. it breaks down if you try to
   have a node with more than 2 values). We can't test on more interesting/complex examples until
   this is fixed. -- Done. The problem was that I was dealing with array size checking incorrectly. 
   The start/stop index + 2 or + 1 is not hardcoded to read in a set number of values, it just moves
   indexing past the value of interest and the comma. Updated the comments to make this clearer.
   
> We need some bigger test examples in a baaaaaaad way (or at least, we will very soon)-- Done. Added 
  12 examples of graph coloring problems, with sizes varying from 5 - 38 nodes. Also varied the examples
  to include different optimal colorings.     
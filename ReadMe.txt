Current TODOs/issues (not necessarily in order of importance)

> Need tree width of graphs

> Find some non-planar graphs

> Compare to greedy algorithm and hillclimb for all applications
   
> Because the factors never really seem to decrease for MN and aren't re-normalized, they just keep getting
   bigger. This might not be an issue, but we should consider finding some way to mitigate this just
   in case. Find a way to scale back factor potential values (maybe /10 every so many iterations?)
   
> Change some of the bad variable names (i.e. potential wrt the tempVal read in, or s for scanner). Got IntegerParticle
  and IntegerParticle done.   
   
> Try using just base probabilities instead of the Boltzmann dist in MOA Gibbs sampling
  
> Ensure that the penalized fitness is reflected in choosing global/local bests, but not recorded
   as results, for all non-MICPSO methods (this was/is a problem with IPSO)
    

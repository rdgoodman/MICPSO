Current TODOs/issues (not necessarily in order of importance)

> Need tree width of graphs

> Find some non-planar graphs

> Compare to specific GC algorithm & greedy

> Test on another problem (note: for 3-sat, will we need clique potentials...?)
   ideas: 3-SAT, MAX-SAT, scheduling
   
> Because the factors never really seem to decrease for MN and aren't re-normalized, they just keep getting
   bigger. This might not be an issue, but we should consider finding some way to mitigate this just
   in case. Find a way to scale back factor potential values (maybe /10 every so many iterations?)
   
> Change some of the bad variable names (i.e. potential wrt the tempVal read in, or s for scanner). Got IntegerParticle
  and IntegerParticle done.   
  
> (Maybe) find a non-evolutionary alternative to test against

> Refactor MN and Edge classes so constraint handling (along with constraint checking) can live 
   in ApplicationProblem class
   
 > Try using just base probabilities instead of the Boltzmann dist in MOA Gibbs sampling
  
 > Ensure that the penalized fitness is reflected in choosing global/local bests, but not recorded
    as results, for all non-MICPSO methods (this was/is a problem with IPSO)
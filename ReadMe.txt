Current TODOs/issues (not necessarily in order of importance)

More Urgent:

> Currently, a lot of our I/O stuff is hard-coded and inflexible (e.g. it breaks down if you try to
   have a node with more than 2 values). We can't test on more interesting/complex examples until
   this is fixed.
   
> The MN particle still needs to be fleshed out [Rollie is making this priority 1 for herself]

> PSO termination criterion is still untested

> We need some bigger test examples in a baaaaaaad way (or at least, we will very soon)




Less Urgent:

> We need to figure out how to (for the MICPSO variant) build/read in the constraints for GC problems.
   Obviously, this can -- and has to -- wait until we get further along on the MICPSO particle
   
> We'll probably need some error/exception-checking to make sure probabilities stay within
   valid boundaries (none < 0 or > 1)
   
> Should build in a way to track the fitness at any given iteration so we can look at fitness trends
   over time for comparison's sake. This should be pretty easy.
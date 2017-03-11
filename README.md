# pcollections

This repo contains implementation of Persistent collection based on Red-Black Tree. 

To build and run tests you need maven to me installed on your machine:
#####mvn clean package

###Links for inspiration:
 - https://en.wikipedia.org/wiki/Red–black_tree
 - http://www.cs.cmu.edu/~sleator/papers/another-persistence.pdf
 - https://ocw.mit.edu/courses/electrical-engineering-and-computer-science/6-854j-advanced-algorithms-fall-2005/lecture-notes/persistent.pdf
 - https://wiki.edinburghhacklab.com/PersistentRedBlackTreeSet
 - https://pcollections.org
 
###Persistence implementation to consider:
  - tree usage as an underlying structure for set vs map -> set
  - full persistence vs. partial -> full one
  - full copy on modification vs. minimal required copy of subtrees -> ???
  
###Other aspects to consider:
  - thread safety on modification -> NO
 
 


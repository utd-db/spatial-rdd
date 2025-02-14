### Spatial RDD
Custom Spark RDD to partition geospatial data, based on spatial proximity, for faster orthogonal range queries.

### What is this Fork about?
This fork modifies the `Spatial RDD` to partition dataset using `KD Tree` & `Epsilon approximation` based on [Parallel Algorithms for Constructing Range and
Nearest-Neighbor Searching Data Structures](https://users.cs.duke.edu/~pankaj/publications/papers/mr-ds.pdf).

### Note
1. Here we have chosen to implement KD tree based on 2D points
2. We are doing `primary partitioning` & `secondary indexing` using `KD Tree`.

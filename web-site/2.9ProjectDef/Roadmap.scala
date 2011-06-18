object Roadmap {

  def html(model:WebsiteModel) = {
    val roadmapContent =
        <div class="explanation">
          <strong>Scala IO Core</strong>
          <p>Scala IO Core will be completed before much more work is done on the FS API so that the library
          won't be forever being developed.  In addition to the major items listed below the common task of
          improving API and removing inconsistency will always be a focus.  As will performance.</p>
          <ol>
              <li><strong>Large Data Integration Tests</strong>
                  <div>Create test sets that are very large (several GB) and verify that the operations can
                  handle the load and work correctly when skipping data</div>
              </li>
              <li><strong>Write Benchmark Tests</strong>
                  <div> Benchmark the integration tests as well as other types of operations (such as
                      large number of small requests on a large dataset).
                      <br/>
                      The idea is to track the performance of each version and ensure it continues to
                      increase each version (and by how much)
                  </div>
              </li>
              <li><strong>Add error handling to resources</strong>
                  <div>
                      At the moment unless a resource is used via Josh's ARM API an error reading from a
                      stream results in an exception being thrown.  I want to allow a user to add an error
                      handler on the resource itself rather than having to use a catch block each time the
                      resource is used
                  </div>
              </li>
              <li><strong>Add Iteratee style IO processing</strong>
                  <div>
                      This is a very flexible way of handling IO with very good compositional properties
                      but for some (who are not familiar with the pattern) it can be less approachable
                  </div>
              </li>
              <li><strong>Add asynchronous callback style IO handling</strong>
                  <div>
                      This will appear much like that which is seen in NodeJS and will be implemented
                      based on the Iteratee IO processing
                  </div>
              </li>
              <li><strong>Implement Java 7 Implementations</strong>
                  <div>This may be raised in the list of priorities depending on how long each task takes.</div>
              </li>
          </ol>
          <strong>Scala IO File</strong>
          <ol>
              <li><strong>Implement ZipFS</strong>
                  <div>
                      The goal is to have several different filesystem implementations that have different
                      characteristics to insure that the API is sufficiently flexible and, most importantly,
                      to make sure that the file system implementation API is flexible.  The usage API is
                      design following NIO2 so I am fairly confident that has sufficient flexibility but the
                      implementer's API is also a very important API.
                  </div>
              </li>
              <li><strong>Implement based on Java 7</strong></li>
              <li><strong>TBD...</strong></li>
          </ol>
        </div>

    Template(false, "Scala IO Development Roadmap")(
        <link href={"css/samples.css"} rel="stylesheet" type="text/css" ></link>)(
        <h1>Scala IO Development Roadmap</h1>)(
        roadmapContent)(Template.rootNavbar(Link.Roadmap,model.projectSites))
  }

}
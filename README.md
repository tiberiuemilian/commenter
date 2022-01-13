# commenter

**Challenge:**
1. Application design
2. Technology stack
3. Implementation draft
   Story:
   Jack has an application idea that he wants to monetize. The application is called "Commenter".
   The users would associate data fragments containing few attributes with any web page found on the
   Internet:
   ```json
   Comment {
      url:"",
      author:"",
      content:"",
      tags:["",""]
   }
   ```
   and allow other users to Comment on their own Comments, nesting possible.
   Functional requirements:
4. Each User should be able to filter Comments by author or tags combined with full text.
5. Each User should be able to navigate Comment trees (multilevel, very large).
6. The Analyst should be able to compute efficiently the number of Comments directly associated with all
   Comments of a given User.
   Non-functional requirements:
7. Eventual consistency required
8. Jack wants that the application can be demonstrated on his laptop but can scale on one thousand
   machines without code changes.

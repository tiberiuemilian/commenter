// libraries import part
const faker = require("faker");
const fs = require('fs');
const { format } = require('@fast-csv/format');

// CSV files declarations
const authorsFile = 'authors.csv';
const csvAuthors = fs.createWriteStream(authorsFile);

const commentsFile = 'comments.csv';
const csvComments = fs.createWriteStream(commentsFile);

const tagsFile = 'tags.csv';
const csvTags = fs.createWriteStream(tagsFile);

// generate authors CSV
const authorStream = format({ headers:true });
// const noOfAuthors = 100_000;
const noOfAuthors = 300;
authorStream.pipe(csvAuthors);
for(let i=1; i<=noOfAuthors; i++) {
    let id = "'" + i + "'";
    let name = "'" + faker.internet.userName() + "'";

    authorStream.write({ id: id, name: name });
}
authorStream.end();

// generate comments CSV
const commentStream = format({ headers:true });
commentStream.pipe(csvComments);
// const noOfComments = 2_000_000;
let noOfComments = 1_000;
const discussionThreads = 250;

// add root comments
let i=1;
for(; i<=discussionThreads; i++) {
    let id = "'" + i + "'";
    let parentId = "NULL";

    let url = "'" + faker.internet.url() + "'";
    let authorId = "'" + Math.floor(Math.random() * noOfAuthors + 1) + "'";
    let content = "'" + faker.lorem.paragraph() + "'";

    commentStream.write({ id: id, parent_id: parentId, url: url, author_id: authorId, content: content });
}

// add replies to other comments
for(; i<=noOfComments; i++) {
    let id = "'" + i + "'";
    let parentId = "'" + Math.floor(Math.random() * i) + "'";

    let url = "'" + faker.internet.url() + "'";
    let authorId = "'" + Math.floor(Math.random() * noOfAuthors + 1) + "'";
    let content = "'" + faker.lorem.paragraph() + "'";

    commentStream.write({ id: id, parent_id: parentId, url: url, author_id: authorId, content: content });
}
commentStream.end();

// generate tags CSV
const tagStream = format({ headers:true });
tagStream.pipe(csvTags);
let tagId = 1;
const maxTagsNr = 10;
for(let i=1; i<=noOfComments; i++) {
    let commentId = "'" + i + "'";

    let tagsNr = Math.floor(Math.random() * maxTagsNr + 1)
    for (let j=1; j<tagsNr; j++, tagId++) {
        // let name = "'" + faker.random.word() + "'";
        let name = "'" + faker.commerce.product() + "'";
        tagStream.write({ id: tagId, comment_id: commentId, name: name });
    }
}
tagStream.end();

// playground for faker data types
// let text;
// for (let i = 0; i < 1000; i++) {
//     text = faker.commerce.product();
//     // text = faker.hacker.noun();
//     // text = faker.lorem.paragraphs();
//     // text = faker.internet.userName();
//     console.log(text);
// }
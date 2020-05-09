const dbcp = require('./dbcp');
const jwt = require('jsonwebtoken');

/*exports.getQueryUser = async (cid) => {
  let conn;
  let result;
  try{
    conn = await dbcp.getConnection();
    const [rows, fields] = await conn.query('SELECT * FROM user WHERE id=? ', [cid]);
    result = rows;
  }catch (error){
    throw error;
  }finally{
    if(conn) await conn.end();
    return result
  }
};//일단 전체적으로 리턴하고 전체적으로 할 수 없다 한다면 auth.js에서 queryUser을 두번 쓴 것을 
//나누어서 사용할것.
*/
exports.loginUser = (req, res, user) => {
  const secret = req.app.get('jwt-secret');

  const sign = (user) => {
    const p = new Promise((resolve, reject) => {
      let exptime = Math.floor(Date.now() / 1000) + (60 * 60 * 24 * 30); // 1달
      jwt.sign(
        {
          userName: user.name,
          exp: exptime,
          iss: 'weknot',
          sub: user.id,
          aud: 'weknot',
          iat: Math.floor(Date.now() / 1000),
        },
        secret,
        (err, token) => {
          if (err) reject(err)
          resolve(token)
        })
    })
    return p;
  }
  return sign(user);
}
exports.getQueryUser = (cid) => {
  const query = (conn) => {
    const p = new Promise((resolve, reject) => {
      conn
        .query('SELECT * FROM user WHERE id=?', [cid])
        .then((result) => {
          conn.end()
          resolve(result)
        })
        .catch((err) => {
          reject(err)
        })
    })
    return p;
  }

  const p = new Promise((resolve, reject) => {
    dbcp.getConnection()
      .then(query)
      .then((result => {
        resolve(result)
      }))
      .catch((err) => { reject(err) })
  })
  return p;
};

exports.allUsers = async () => {
  let conn;
  let result;
  try {
    conn = await dbcp.getConnection();
    result = await conn.query("select * from user ");
  } catch (err) {
    throw err;
  } finally {
    if (conn) await conn.end();
    return result;
  }
};

/*exports.registerUser = async (user) => {
  let conn;
  const sql = "INSERT INTO user(id,name,password,birth,gender,phoneNumber) VALUES (?,?,?,?,?,?) ";
  let isSuccess = true;
  try{
    conn = dbcp.getConnection();
    await conn.query(sql,user);
  }catch (error){
    console.log(error);
    isSuccess = !isSuccess;
    throw new error;
  }finally{
    if(conn) await conn.end();
    return isSuccess;
  }
};*/

exports.registerUser = (user) => {
  console.log(user);
  const sql = 'INSERT INTO user(id,name,password,birth,gender,phoneNumber, photo, intro) VALUES (?,?,?,?,?,?,?,?)';
  const query = (conn) => {
    const p = new Promise((resolve, reject) => {
      conn
        .query(sql, [user.id, user.name, user.password, user.birth, user.gender, user.phoneNumber, user.photo, user.intro])
        .then((result) => {
          conn.end()
          console.log(result)
          resolve(result)
        })
        .catch((err) => {
          console.log(err);
          reject(err)
        })
    })
    return p;
  }

  const p = new Promise((resolve, reject) => {
    dbcp.getConnection()
      .then(query)
      .then((result => {
        resolve(result)
      }))
      .catch((err) => { reject(err) })
  })
  return p;
};

/*exports.transSmsMessage = (req,res) => {
 const phoneNumber = "010-9121-0769"

  request({
    method: 'POST',
    json: true,
    uri: `https://api-sens.ncloud.com/v1/sms/services/${process.env.SENS_SERVICEID}/messages`,
    headers: {
      'Content-Type': 'application/json',
      'X-NCP-auth-key': process.env.SENS_ACCESSKEYID,
      'X-NCP-service-secret': process.env.SENS_SERVICESECRET
    },
    body: {
      type: 'sms',
      from: process.env.SENS_SENDNUMBER,
      to: [`${phoneNumber}`],
      content: `WeGoing 인증번호 341353입니다.`
    }
  });
}*/

exports.showUserProfile = (cid) => {
  const sql = 'SELECT id,name,birth,gender,photo,intro,scope,point FROM user WHERE id = ?';
  const query = (conn) => {
    const p = new Promise((resolve, reject) => {
      conn
        .query(sql, cid)
        .then((result) => {
          conn.end()
          resolve(result)
        })
        .catch((err) => {
          reject(err)
        })
    })
    return p;
  }

  const p = new Promise((resolve, reject) => {
    dbcp.getConnection()
      .then(query)
      .then((result => {
        resolve(result)
      }))
      .catch((err) => { reject(err) })
  })
  return p;
};

/*exports.addFriend = async (idList) => {//requester랑receiver가 db값과 둘다 같다면?  
  let conn;
  const sql = "INSERT INTO friends(requester,receiver,state) VALUES (?,?,?) "
  let isSuccess = true;
  try{
    conn = await dbcp.getConnection();
    await conn.query(sql,idList);
  }catch (error){
    isSuccess = !isSuccess;
    throw new error;
  }finally{
    if(conn) await conn.end();
    return isSuccess;
  }
}*/

exports.addFriend = (idList) => {
  const sql = 'INSERT INTO friends(requester,receiver,state) VALUES (?,?,?)';
  const query = (conn) => {
    const p = new Promise((resolve, reject) => {
      conn
        .query(sql, idList)
        .then((result) => {
          conn.end()
          console.log(result)
          resolve(result)
        })
        .catch((err) => { reject(err) })
    })
    return p;
  }

  const p = new Promise((resolve, reject) => {
    dbcp.getConnection()
      .then(query)
      .then((result => {
        resolve(result)
      }))
      .catch((err) => { reject(err) })
  })
  return p;
};

// exports.acceptFriend = (knot) => {
//   const sql = "UPDATE friends SET state = 1 WHERE requester = ? and receiver = ?"
//   const query = (conn)=> {
//     const p = new Promise((resolve, reject) => {
//       conn
//         .query(sql,[knot.userId,knot.friend])
//         .then((result) => {
//           conn.end()
//           console.log(result)
//           resolve(result)
//         })
//         .catch((err) =>{reject(err)})
//     })
//   }
//   const p = new Promise((resolve, reject) => {
//     dbcp.getConnection()
//       .then(query)
//       .then((result=> {
//         resolve(result)
//       }))
//       .catch((err) => {reject(err)})
//   })
//   return p;
// }

exports.acceptFriend = async (knot) => {
  //조회해서 이미 state=1인지 알아봐야함.
  const sql = "UPDATE friends SET state = 1 WHERE receiver = ? and requester = ?"
  console.log(knot);
  let result;
  try {
    let conn = await dbcp.getConnection();
    result = await conn.query(sql, [knot.userId, knot.friend]);
  } catch (error) {
    throw new error;
  } finally {
    if (conn) await conn.end();
    return result;
  }
}

exports.refuseFriend = async (knot) => {
  let conn;
  const sql = "DELETE FROM friends WHERE receiver = ? and requester = ?"
  let result;
  try {
    let conn = await dbcp.getConnection();
    result = await conn.query(sql, [knot.userId, knot.friend]);
  } catch (error) {
    console.log(error);
    throw new error;
  } finally {
    if (conn) await conn.end();
    return result;
  }
}

exports.getFriendState = async (userId, myId) => {
  let conn;
  const sql = "select * from friends where (requester=? and receiver=?) or (requester=? and receiver=?)"
  let result;
  try {
    let conn = await dbcp.getConnection();
    result = await conn.query(sql, [userId, myId, myId, userId]);
    console.log(result);
    if (result.length == 0)
      result = 0;
    else if (result[0].state == 1)
      result = 10;
    else if (result[0].requester == myId)
      result = 2;
    else
      result = 1;
  } catch (error) {
    throw new error;
  } finally {
    if (conn) await conn.end();
    return result;
  }
}

exports.getFriend = async (requester) => {
  const sql = '(SELECT user.id as friendId, user.name as friendName, user.point as friendPoint, user.photo as friendPhoto, friends.state as friendState from user ' +
    'JOIN friends ' +
    'ON user.id = friends.receiver ' +
    'WHERE requester = ? AND state = 1) ' +
    'Union ' +
    '(SELECT user.id as friendId, user.name as friendName, user.point as friendPoint, user.photo as friendPhoto, friends.state as friendState from user ' +
    'JOIN friends ' +
    'ON user.id = friends.requester ' +
    'WHERE receiver = ?) '
  'ORDER BY name ASC' +
    console.log(requester);
  console.log(sql);
  let result;
  try {
    let conn = await dbcp.getConnection();
    result = await conn.query(sql, [requester, requester]);
  } catch (error) {
    throw new error;
  } finally {
    if (conn) await conn.end();
    return result;
  }
}

//검사할때 쓸 함수
getFriends = async (requester) => {
  let conn;
  const sql = "SELECT receiver FROM friends WHERE requester = ? and state = 1"
  let result;
  try {
    let conn = await dbcp.getConnection();
    result = await conn.query(sql, requester);
  } catch (error) {
    throw new error;
  } finally {
    if (conn) await conn.end();
    return result;
  }
}

exports.getDms = async (cid) => {
  let conn;
  const sql = "SELECT id,sender,message,time,isRead FROM dm WHERE receiver = ? "
  let dmResult;
  try {
    conn = await dbcp.getConnection();
    const [rows, fields] = await conn.query(sql, cid);
    dmResult = rows;
  } catch (error) {
    throw error;
  } finally {
    if (conn) await conn.end();
    return dmResult;
  }
}

exports.getPicture = async (cid) => {
  let conn;
  const sql = "SELECT picture FROM user WHERE id = ? "
  let pictureResult;
  try {
    conn = await dbcp.getConnection();
    await conn.query(sql, cid, (err, result, fields) => {
      if (err) throw err;
      console.log(result);
      pictureResult = result;
    });
    //   console.log(rows);
  } catch (error) {
    throw error;
  } finally {
    if (conn) await conn.end();
    return pictureResult;
  }
}

//feed에서 like했을 경우만 가능하다.
exports.modifyFeedPoint = async (datas) => {
  let conn, sql, result;
  if (datas.modify === '+') {
    sql =  `UPDATE user A INNER JOIN \`like\` B ON A.id = B.receiver SET A.point = A.point+1 WHERE B.sender = ${datas.sender} AND B.feedId = ${datas.feedId};`;
  } else {
    sql = `UPDATE user A INNER JOIN \`like\` B ON A.id = B.receiver SET A.point = A.point-1 WHERE B.id = ${datas.likeId};`;
  }
  try{
    console.log(sql);
    conn = await dbcp.getConnection();
    result = await conn.query(sql);
  } catch (error) {
    throw error;
  }finally {
    if (conn) await conn.end();
    return result;
  }
}

exports.modifyUserPoint = async (cid) => {
  let conn;
  let result;
  const sql = 'UPDATE user SET user.point= user.point+1 WHERE user.id= ?';
  try {
    conn = await dbcp.getConnection();
    result = await conn.query(sql, cid)
  }
  catch (error) {
    throw new error;
  } finally {
    if (conn) await conn.end();
    return result;
  }
}
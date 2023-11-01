print('Start init script')
db.createUser(
        {
            user: "logistapi",
            pwd: "logistapi",
            roles: [
                {
                    role: "readWrite",
                    db: "logist-api-order"
                }
            ]
        }
);
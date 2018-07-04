module Users.UserLookup where 

import Prelude

import Data.Argonaut (class DecodeJson, class EncodeJson, decodeJson, encodeJson, jsonEmptyObject, (.?), (.??), (:=), (~>))
import Data.Either (either)
import Data.Maybe (Maybe)
import Data.Newtype (class Newtype)
import EQUELLA.Environment (baseUrl)
import Effect.Aff (Aff, error, throwError)
import Global.Unsafe (unsafeEncodeURIComponent)
import Network.HTTP.Affjax (get, post)
import Network.HTTP.Affjax.Response (json)
import Network.HTTP.Affjax.Request (json) as Req

type UserDetailsR = {id:: String, username:: String, firstName:: String,
                           lastName:: String, email :: Maybe String }
                           
newtype UserDetails = UserDetails UserDetailsR
derive instance ntUD :: Newtype UserDetails _
derive instance eqUD :: Eq UserDetails

type IdName = {id:: String, name:: String }
newtype GroupDetails = GroupDetails IdName
derive instance ntGD :: Newtype GroupDetails _
derive instance eqGD :: Eq GroupDetails

newtype RoleDetails = RoleDetails IdName
derive instance ntRD :: Newtype RoleDetails _
derive instance eqRD :: Eq RoleDetails

newtype UserGroupRoles u g r = UserGroupRoles {users:: Array u, groups :: Array g, roles:: Array r}

class ToUGRDetail a where 
  toUGR :: a -> UGRDetail

type UGRDetail = UserGroupRoles UserDetails GroupDetails RoleDetails 

instance semigroupUGR :: Semigroup (UserGroupRoles u g r) where 
  append (UserGroupRoles a) (UserGroupRoles b) = UserGroupRoles {users:a.users <> b.users, groups:a.groups <> b.groups, roles: a.roles <> b.roles }

instance monoidUGR :: Monoid (UserGroupRoles u g r) where 
  mempty = UserGroupRoles {users:[], groups:[], roles:[]}

instance encUserGroupRoles :: (EncodeJson u, EncodeJson g, EncodeJson r) => EncodeJson (UserGroupRoles u g r) where
  encodeJson (UserGroupRoles {users, groups, roles}) = 
    "users" := users ~>
    "groups" := groups ~> 
    "roles" := roles ~> jsonEmptyObject

instance decUserGroupRoles :: (DecodeJson u, DecodeJson g, DecodeJson r) => DecodeJson (UserGroupRoles u g r) where
  decodeJson v = do 
    o <- decodeJson v
    users <- o .? "users"
    groups <- o .? "groups"
    roles <- o .? "roles"
    pure $ UserGroupRoles {users,groups,roles}

instance decUserDetails :: DecodeJson UserDetails where 
  decodeJson v = do 
    o <- decodeJson v
    id <- o .? "id"
    username <- o .? "username"
    firstName <- o .? "firstName"
    lastName <- o .? "lastName"
    email <- o .?? "email"
    pure $ UserDetails {id,username,firstName,lastName, email}

instance decGroupDetails :: DecodeJson GroupDetails where 
  decodeJson v = do 
    o <- decodeJson v
    id <- o .? "id"
    name <- o .? "name"
    pure $ GroupDetails {id,name}
  
instance decRoleDetails :: DecodeJson RoleDetails where 
  decodeJson v = do 
    o <- decodeJson v
    id <- o .? "id"
    name <- o .? "name"
    pure $ RoleDetails {id,name}

instance uUGR :: ToUGRDetail UserDetails where 
  toUGR u = UserGroupRoles {users:[u], groups:[], roles:[]}

instance gUGR :: ToUGRDetail GroupDetails where 
  toUGR g = UserGroupRoles {users:[], groups:[g], roles:[]}

instance rUGR :: ToUGRDetail RoleDetails where 
  toUGR r = UserGroupRoles {users:[], groups:[], roles:[r]}

lookupUsers :: UserGroupRoles String String String -> Aff UGRDetail
lookupUsers r = do 
  resp <- post json (baseUrl <> "api/userquery/lookup") $ Req.json (encodeJson r)
  either (throwError <<< error) pure $ decodeJson resp.response

searchUGR :: String -> {users::Boolean, groups :: Boolean, roles :: Boolean} -> Aff UGRDetail
searchUGR q {users,groups,roles} =   do 
  let param t b = "&" <> t <> "=" <> show b
  resp <- get json $ baseUrl <> "api/userquery/search?q=" <> unsafeEncodeURIComponent q <> param "users" users 
              <> param "groups" groups <> param "roles" roles 
  either (throwError <<< error) pure $ decodeJson resp.response

listTokens :: Aff (Array String)
listTokens =  do 
  resp <- get json $ baseUrl <> "api/userquery/tokens"
  either (throwError <<< error) pure $ decodeJson resp.response

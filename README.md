# ChatColor
Plugin for Spigot that allows you to create patterns and colors for the chat

##Commands

| Users                    | Permission       |
|:-------------------------|:-----------------:|
| /chatcolor               |                   |
| /chatcolor set [pattern] | chatcolor.set     |
| /chatcolor list          | chatcolor.list    |
| /chatcolor disable       | chatcolor.disable |
| /chatcolor gui           | chatcolor.gui     |


| Admin                                  | Permission             |
|:---------------------------------------|:-----------------------:|
| /chatcoloradmin                        |                         |
| /chatcoloradmin set [player] [pattern] | chatcolor.admin.set     |
| /chatcoloradmin disable [player]       | chatcolor.admin.disable |
| /chatcoloradmin gui [player]           | chatcolor.admin.gui     |
| /chatcoloradmin reload                 | chatcolor.admin.reload  |

##Pattern Modes

| Identifier           | Description                                                       |
|:---------------------|:-----------------------------------------------------------------:|
| SINGLE               | Use only the first color                                          |
| RANDOM               | Use the colors randomly                                           |
| LINEAR               | Utiliza todos los colores de manera linear                        |
| LINEAR_IGNORE_SPACES | Utiliza todos los colores de manera linear ignorando los espacios |
| GRADIENT             | Hace un gradiente con los colores ingresados                      |

## Placeholders

| Placeholder                        | Information                                                                  |
|:-----------------------------------|:-----------------------------------------------------------------------------|
| %chatcolor_pattern_name%           | Returns the name of the pattern that the equipped player has                 |
| %chatcolor_pattern_name_formatted% | Returns the name of the pattern that the player has equipped but with colors |
